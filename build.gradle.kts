plugins {
    kotlin("jvm") version "1.9.21"
    `java-gradle-plugin`
}

group = "io.github.sunnhas"
version = "1.2.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

kotlin {
    jvmToolchain(11)
}
