import io.github.sunnhas.poeditor.config.Format

plugins {
    id("io.github.sunnhas.po-gradle")
}

val poeditorApiToken: String by project

poGradle {
    apiToken.set(poeditorApiToken)

    projects {
        create("all") {
            projectId.set(681529)
            languages.set(listOf("en", "da"))
            format.set(Format.JSON)
            output.set(projectDir.resolve("translations/all"))
        }

        create("testTag") {
            projectId.set(681529)
            tags.set(listOf("test-tag"))
            languages.set(listOf("en", "da"))
            format.set(Format.PROPERTIES)
            output.set(projectDir.resolve("translations/test-tag"))
        }
    }
}
