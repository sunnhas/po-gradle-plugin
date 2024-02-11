package io.github.sunnhas.poeditor

import io.github.sunnhas.poeditor.config.PoGradleExtension
import io.github.sunnhas.poeditor.tasks.DownloadExportTask
import io.github.sunnhas.poeditor.tasks.ListProjectsTask
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import kotlin.reflect.KClass

class PoGradlePluginTest : BehaviorSpec({

    given("an empty Gradle project") {

        val project: Project = ProjectBuilder.builder().build()

        `when`("the poeditor plugin is applied") {
            project.pluginManager.apply("io.github.sunnhas.po-gradle")

            then("the extension `poeditor` should accessible") {
                project.extensions.getByName("poGradle") shouldBeOfType PoGradleExtension::class
            }

            then("the task `poeditorProjects` exists") {
                project.tasks.getByName("poProjects") shouldBeOfType ListProjectsTask::class
            }

            `when`("the plugin is configured with a project and two languages") {
                val poGradleExtension = project.extensions.getByType(PoGradleExtension::class.java)
                poGradleExtension.projects.create("projectName") {
                    it.languages.set(listOf("en", "da"))
                }

                then("the task `poeditorDownloadProjectName` exists") {
                    project.tasks.getByName("poDownloadProjectName") shouldBeOfType DownloadExportTask::class
                }

                then("the task `poeditorDownloadProjectNameEn` exists") {
                    project.afterEvaluate {
                        project.tasks.getByName("poDownloadProjectNameEn") shouldBeOfType DownloadExportTask::class
                    }
                }

                then("the task `poeditorDownloadProjectNameDa` exists") {
                    project.afterEvaluate {
                        project.tasks.getByName("poDownloadProjectNameDa") shouldBeOfType DownloadExportTask::class
                    }
                }
            }
        }
    }

})

fun <T : Any, R : Any> beOfType(type: KClass<R>) = Matcher<T> { value ->
    MatcherResult(
        type.isInstance(value),
        { "Value was ${value::class.qualifiedName} but should be ${type.qualifiedName}" },
        { "Value should not be ${value::class.qualifiedName}" }
    )
}

infix fun <T : Any, R : Any> T.shouldBeOfType(type: KClass<R>): T {
    this should beOfType(type)
    return this
}

infix fun <T : Any, R : Any> T.shouldNotBeOfType(type: KClass<R>): T {
    this shouldNot beOfType(type)
    return this
}
