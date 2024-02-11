package io.github.sunnhas.poeditor.config

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.OutputDirectory
import java.io.File

abstract class ProjectExtension {
    abstract val name: String
    abstract val projectId: Property<Long>
    abstract val languages: ListProperty<String>
    abstract val format: Property<Format>
    abstract val tags: ListProperty<String>

    @get:OutputDirectory
    abstract val output: Property<File>
}