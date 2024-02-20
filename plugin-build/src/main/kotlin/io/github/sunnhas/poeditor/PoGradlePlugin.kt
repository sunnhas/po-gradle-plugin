package io.github.sunnhas.poeditor

import io.github.sunnhas.poeditor.config.PoGradleExtension
import io.github.sunnhas.poeditor.config.ProjectExtension
import io.github.sunnhas.poeditor.tasks.AnalyseTermsTasks
import io.github.sunnhas.poeditor.tasks.DownloadExportTask
import io.github.sunnhas.poeditor.tasks.ListProjectsTask
import io.github.sunnhas.poeditor.tasks.UpdateTermsTask
import io.github.sunnhas.poeditor.util.capitalized
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.SourceSetContainer
import java.io.File

class PoGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = project.extensions.create("poGradle", PoGradleExtension::class.java)

        project.tasks.register("poProjects", ListProjectsTask::class.java) { task ->
            task.apiToken.set(config.apiToken)
        }

        config.projects.all { p ->
            project.registerDownloadTask("poDownload${p.name.capitalized()}", config.apiToken, p)
            project.registerUpdateTask("poUpdate${p.name.capitalized()}", config.apiToken, p)

            project.afterEvaluate {
                p.languages.get().forEach {
                    project.registerDownloadTask(
                        name = "poDownload${p.name.capitalized()}${it.capitalized()}",
                        apiToken = config.apiToken,
                        projectExtension = p,
                    ) { task ->
                        task.languages.set(listOf(it))
                    }

                    project.registerUpdateTask(
                        name = "poUpdate${p.name.capitalized()}${it.capitalized()}",
                        apiToken = config.apiToken,
                        projectExtension = p,
                    ) { task ->
                        task.languages.set(listOf(it))
                    }
                }
            }

            // Only if we have a plugin defining source sets to check files on
            val analyseSource = p.getAnalyseSource(project)
            if (!analyseSource.isNullOrEmpty()) {
                project.tasks.register("poAnalyse${p.name.capitalized()}", AnalyseTermsTasks::class.java) { task ->
                    task.apiToken.set(config.apiToken)
                    task.projectId.set(p.projectId)
                    task.tags.set(p.tags)
                    task.analysePattern.set(p.analysePattern)
                    task.analyseSource.set(analyseSource)
                }
            }
        }
    }
    
    private fun Project.registerDownloadTask(
        name: String,
        apiToken: Property<String>,
        projectExtension: ProjectExtension,
        configureAction: (DownloadExportTask) -> Unit = {},
    ) {
        tasks.register(name, DownloadExportTask::class.java) { task ->
            task.apiToken.set(apiToken)
            task.projectId.set(projectExtension.projectId)
            task.languages.set(projectExtension.languages)
            task.format.set(projectExtension.format)
            task.tags.set(projectExtension.tags)
            task.output.set(projectExtension.output)
            task.fileNamePattern.set(projectExtension.fileNamePattern)

            configureAction(task)
        }
    }

    private fun Project.registerUpdateTask(
        name: String,
        apiToken: Property<String>,
        projectExtension: ProjectExtension,
        configureAction: (UpdateTermsTask) -> Unit = {},
    ) {
        tasks.register(name, UpdateTermsTask::class.java) { task ->
            task.apiToken.set(apiToken)
            task.projectId.set(projectExtension.projectId)
            task.languages.set(projectExtension.languages)
            task.format.set(projectExtension.format)
            task.tags.set(projectExtension.tags)
            task.output.set(projectExtension.output)
            task.fileNamePattern.set(projectExtension.fileNamePattern)

            configureAction(task)
        }
    }

    private fun ProjectExtension.getAnalyseSource(project: Project): Collection<File>? {
        return if (analyseSource.isPresent) {
            analyseSource.get().files
        } else {
            project.extensions
                .findByType(SourceSetContainer::class.java)
                ?.flatMap { it.allSource.asFileTree.files }
        }
    }
}
