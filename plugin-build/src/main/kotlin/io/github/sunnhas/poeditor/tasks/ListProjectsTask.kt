package io.github.sunnhas.poeditor.tasks

import io.github.sunnhas.poeditor.client.api.projects.getProjects
import kotlinx.coroutines.runBlocking
import org.gradle.api.tasks.TaskAction

abstract class ListProjectsTask : PoGradleTask() {

    @TaskAction
    fun run() {
        val projectResponse = runBlocking {
            createClient().use { client ->
                client.getProjects()
            }
        }

        projectResponse.projects.forEach {
            project.logger.lifecycle("-> ${it.name} (${it.id})")
        }
    }

}
