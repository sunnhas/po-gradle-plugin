package io.github.sunnhas.poeditor.tasks

import io.github.sunnhas.poeditor.client.api.terms.getTerms
import kotlinx.coroutines.runBlocking
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction

abstract class AnalyseTermsTasks : PoGradleTask() {

    @get:Input
    abstract val projectId: Property<Long>

    @get:Input
    @get:Optional
    abstract val analysePattern: Property<String>

    @get:Input
    abstract val tags: ListProperty<String>

    @TaskAction
    fun run() {
        val terms = runBlocking {
            createClient().use {
                it.getTerms(projectId.get())
            }
        }

        val basePattern = analysePattern.orNull ?: "[\"']{{term}}[\"']"
        val filterTags = tags.get()

        val patterns = terms.terms
            .filter { filterTags.isEmpty() || it.tags.any { filterTags.contains(it) } }
            .map {
                TermContainer(
                    it.term,
                    basePattern.replace("{{term}}", it.term.replace(".", "\\.")).toRegex()
                )
            }

        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)

        sourceSets.forEach { sourceSet ->
            sourceSet.allSource.asFileTree.forEach { file ->
                val content = file.readText().split("\n")

                patterns.forEach { tc ->
                    content.forEachIndexed { index, s ->
                        tc.regex.findAll(s).forEach { result ->
                            tc.result.add(
                                Result(
                                    file = file.name,
                                    line = index + 1,
                                    range = result.range,
                                )
                            )
                        }
                    }
                }
            }
        }

        logger.lifecycle("Terms not in use (count: ${patterns.count { !it.hasResults }})")
        patterns.filter { !it.hasResults }
            .forEach { tc ->
                logger.lifecycle("-> ${tc.term}")
            }

        logger.lifecycle("")
        logger.lifecycle("Terms in use (count: ${patterns.count { it.hasResults }})")
        patterns.filter { it.hasResults }
            .forEach { tc ->
                logger.lifecycle("-> ${tc.term}")
                tc.result.forEach {
                    logger.lifecycle("  - ${it.file}:${it.line}:${it.range}")
                }
            }
    }


    private data class TermContainer(
        val term: String,
        val regex: Regex,
        val result: MutableList<Result> = mutableListOf(),
    ) {
        val hasResults: Boolean
            get() = result.isNotEmpty()
    }

    private data class Result(
        val file: String,
        val line: Int,
        val range: IntRange,
    )
}