import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    `maven-publish`
}

group = "com.nesterovich.alexey"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

// change version of kotlin in the code so that it is compatible with older version
tasks.withType<KotlinCompile> {
    kotlinOptions {
        languageVersion = when {
            name.contains("test", ignoreCase = true) -> "1.7"
            else -> "1.5"
        }
        jvmTarget = "1.8"
    }
}

// change kotlin version in dependencies, because by default use version equal to plugin version
configurations.configureEach {
    withDependencies {
        filterIsInstance<ExternalDependency>()
            .filter { it.group == "org.jetbrains.kotlin" }
            .forEach {
                val version = when {
                    name.contains("test", ignoreCase = true) || name == "kotlinCompilerClasspath" -> "1.7.21"
                    else -> "1.5.32"
                }
                it.version { require(version) }
            }
    }
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            from(components["java"])
        }
    }
}