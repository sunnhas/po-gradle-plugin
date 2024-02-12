package io.github.sunnhas.poeditor

import io.github.sunnhas.poeditor.config.PoGradleExtension
import io.github.sunnhas.poeditor.tasks.AnalyseTermsTasks
import io.github.sunnhas.poeditor.tasks.DownloadExportTask
import io.github.sunnhas.poeditor.tasks.ListProjectsTask
import io.github.sunnhas.poeditor.util.capitalized
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer

class PoGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = project.extensions.create("poGradle", PoGradleExtension::class.java)

        project.tasks.register("poProjects", ListProjectsTask::class.java) { task ->
            task.apiToken.set(config.apiToken)
        }

        config.projects.all { p ->
            val taskName = "poDownload${p.name.capitalized()}"
            project.tasks.register(taskName, DownloadExportTask::class.java) { task ->
                task.apiToken.set(config.apiToken)
                task.projectId.set(p.projectId)
                task.languages.set(p.languages)
                task.format.set(p.format)
                task.tags.set(p.tags)
                task.output.set(p.output)
                task.fileNamePattern.set(p.fileNamePattern)
            }

            project.afterEvaluate {
                p.languages.get().forEach {
                    val tname = "poDownload${p.name.capitalized()}${it.capitalized()}"
                    project.tasks.register(tname, DownloadExportTask::class.java) { task ->
                        task.apiToken.set(config.apiToken)
                        task.projectId.set(p.projectId)
                        task.languages.set(listOf(it))
                        task.format.set(p.format)
                        task.tags.set(p.tags)
                        task.output.set(p.output)
                        task.fileNamePattern.set(p.fileNamePattern)
                    }
                }
            }

            // Only if we have a plugin defining source sets to check files on
            if (project.extensions.findByType(SourceSetContainer::class.java) != null) {
                project.tasks.register("poAnalyse${p.name.capitalized()}", AnalyseTermsTasks::class.java) { task ->
                    task.apiToken.set(config.apiToken)
                    task.projectId.set(p.projectId)
                    task.analysePattern.set(p.analysePattern)
                    task.tags.set(p.tags)
                }
            }
        }
    }
}
