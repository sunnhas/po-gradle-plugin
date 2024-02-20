package io.github.sunnhas.poeditor.tasks

import io.github.sunnhas.poeditor.client.PoeditorClient
import io.github.sunnhas.poeditor.client.api.terms.request.CreateTerm
import io.github.sunnhas.poeditor.client.api.terms.createTerms
import io.github.sunnhas.poeditor.client.api.terms.getTerms
import io.github.sunnhas.poeditor.client.api.terms.response.TermsResponse
import io.github.sunnhas.poeditor.client.api.translations.request.UpdateTranslation
import io.github.sunnhas.poeditor.client.api.translations.updateTranslation
import io.github.sunnhas.poeditor.config.Format
import io.github.sunnhas.poeditor.parser.FormatFileParser
import io.github.sunnhas.poeditor.parser.Term
import kotlinx.coroutines.runBlocking
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class UpdateTermsTask : PoGradleTask() {

    @get:Input
    abstract val projectId: Property<Long>

    @get:Input
    abstract val languages: ListProperty<String>

    @get:Input
    abstract val format: Property<Format>

    @get:Input
    abstract val tags: ListProperty<String>

    @get:Input
    abstract val output: Property<File>

    @get:Input
    @get:Optional
    abstract val fileNamePattern: Property<String?>

    @TaskAction
    fun run() {
        languages.get().forEach { lang ->
            logger.lifecycle("Processing ${lang}...")

            val termsLocal = parseLocalTerms(lang)
            val termsRemote = getRemoteTerms(lang)

            val termsToAdd = termsLocal.filter {
                t -> termsRemote.all { it.term != t.term }
            }

            val termsToUpdate = termsLocal.filter { t ->
                termsRemote.any {
                    it.term == t.term && it.translation?.content != t.definition
                }
            }

            logger.lifecycle("Terms to create: ${termsToAdd.map { it.term }}")
            logger.lifecycle("Terms to update: ${termsToUpdate.map { it.term }}")

            val doUpdate = project.hasProperty("po.update")
            if (!doUpdate) {
                logger.lifecycle("To apply updates, add the following property: -Dpo.update=true")
                return@forEach
            }

            logger.lifecycle("Performing updates...")

            if (termsToAdd.isNotEmpty()) {
                createTerms(termsToAdd)
                updateTranslations(termsToAdd, lang)
            }

            if (termsToUpdate.isNotEmpty()) {
                updateTranslations(termsToUpdate, lang)
            }
        }
    }

    private fun parseLocalTerms(lang: String): List<Term> {
        val fileNamePattern = (fileNamePattern.orNull ?: "translations_%s") + ".${format.get().ext}"
        val targetFile = output.get().resolve(fileNamePattern.format(lang))
        val parser = FormatFileParser.of(format.get())

        return parser.parse(targetFile)
    }

    private fun getRemoteTerms(lang: String): List<TermsResponse.Term> {
        val filterTags = tags.get()

        return useClient { client ->
            client.getTerms(projectId.get(), lang)
                .terms
                .filter { filterTags.isEmpty() || it.tags.any { filterTags.contains(it) } }
        }
    }

    private fun createTerms(terms: List<Term>) {
        val termsCreatedResponse = useClient { client ->
            val termsData = terms.map {
                CreateTerm(
                    term = it.term,
                    context = it.context,
                    tags = tags.orNull.orEmpty() + it.tags.orEmpty(),
                )
            }

            client.createTerms(projectId.get(), termsData)
        }

        logger.lifecycle("Created terms: ${termsCreatedResponse.terms.added}")
    }

    private fun updateTranslations(terms: List<Term>, lang: String) {
        val updateTranslationsResponse = useClient { client ->
            val translationsData = terms.map {
                UpdateTranslation(
                    term = it.term,
                    translation = UpdateTranslation.Translation(
                        content = it.definition ?: "",
                    )
                )
            }

            client.updateTranslation(projectId.get(), lang, translationsData)
        }

        logger.lifecycle("Updated translations: ${updateTranslationsResponse.translations.updated}")
    }

    private fun <T> useClient(block: suspend (PoeditorClient) -> T): T {
        return runBlocking {
            createClient().use { block(it) }
        }
    }
}
