plugins {
    kotlin("jvm") version "1.9.21"
    `java-gradle-plugin`
}

group = "io.github.sunnhas"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
}

gradlePlugin {
    plugins {
        create("po-gradle") {
            id = "io.github.sunnhas.po-gradle"
            implementationClass = "io.github.sunnhas.poeditor.POGradlePlugin"
        }
    }
}

val functionalTest by sourceSets.creating

val kotestVersion = "5.8.0"

dependencies {
    implementation(gradleApi())

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
