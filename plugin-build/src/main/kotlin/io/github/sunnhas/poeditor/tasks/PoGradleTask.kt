package io.github.sunnhas.poeditor.tasks

import io.github.sunnhas.poeditor.client.PoeditorClient
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

abstract class PoGradleTask : DefaultTask() {

    init {
        group = "poeditor"
    }

    @get:Input
    abstract val apiToken: Property<String>

    internal fun createClient() = PoeditorClient(apiToken.get(), logger)

}