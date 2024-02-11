package io.github.sunnhas.poeditor.tasks

import io.github.sunnhas.poeditor.client.api.projects.request.ExportType
import io.github.sunnhas.poeditor.client.download
import io.github.sunnhas.poeditor.client.api.projects.getExportLink
import io.github.sunnhas.poeditor.config.Format
import kotlinx.coroutines.runBlocking
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class DownloadExportTask : PoGradleTask() {

    @get:Input
    abstract val projectId: Property<Long>

    @get:Input
    abstract val languages: ListProperty<String>

    @get:Input
    abstract val format: Property<Format>

    @get:Input
    abstract val tags: ListProperty<String>

    @get:OutputDirectory
    abstract val output: Property<File>

    @TaskAction
    fun run() = runBlocking {
        createClient().use { client ->
            languages.get().forEach { lang ->
                val targetFile = output.get().resolve("translations_$lang.${format.get().ext}")

                val export = client.getExportLink(
                    projectId = projectId.get(),
                    languageCode = lang,
                    exportType = format.get().asExportType,
                    tags = tags.orNull,
                )

                client.download(export.url, into = targetFile)

                project.logger.lifecycle("Downloaded $lang for ${projectId.get()} into ${targetFile.relativeTo(project.projectDir)}")
            }
        }
    }

    private val Format.asExportType
        get() = when (this) {
            Format.JSON -> ExportType.JSON
            Format.PROPERTIES -> ExportType.PROPERTIES
        }

}