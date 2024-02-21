plugins {
    kotlin("jvm") version "1.9.21"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
}

group = "io.github.sunnhas"
version = "1.2.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
}

gradlePlugin {
    website = "https://github.com/sunnhas/po-gradle-plugin"
    vcsUrl = "https://github.com/sunnhas/po-gradle-plugin"

    plugins {
        create("po-gradle") {
            id = "io.github.sunnhas.po-gradle"
            displayName = "PoGradle"
            description = "Gradle plugin for integrating with PoEditor"
            tags = listOf("poeditor", "i18n", "localization", "translation", "translation-management")
            implementationClass = "io.github.sunnhas.poeditor.PoGradlePlugin"
        }
    }
}

val functionalTest by sourceSets.creating

val ktorVersion = "2.3.7"
val kotestVersion = "5.8.0"

dependencies {
    implementation(gradleApi())
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    "functionalTestImplementation"(project)
    "functionalTestImplementation"(gradleTestKit())
    "functionalTestImplementation"("io.kotest:kotest-runner-junit5:$kotestVersion")
    "functionalTestImplementation"("io.kotest:kotest-assertions-core:$kotestVersion")
}

val functionalTestTask = tasks.register<Test>("functionalTest") {
    description = "Runs the functional tests."
    group = "verification"
    testClassesDirs = functionalTest.output.classesDirs
    classpath = functionalTest.runtimeClasspath
    mustRunAfter(tasks.test)
}

tasks.check {
    dependsOn(functionalTestTask)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
