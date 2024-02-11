package io.github.sunnhas.poeditor.config

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Property

abstract class PoGradleExtension {

    abstract val apiToken: Property<String>

    abstract val projects: NamedDomainObjectContainer<ProjectExtension>

}