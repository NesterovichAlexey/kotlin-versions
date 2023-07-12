import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library") version "7.4.2"
    kotlin("android") version "1.7.21"
    `maven-publish`
}

group = "com.nesterovich.alexey"
version = "1.0-SNAPSHOT"

android {
    namespace = "com.nesterovich.alexey"
    compileSdk = 33

    defaultConfig {
        minSdk = 14

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("debug")
        named("release")
        create("snapshot")
    }

    flavorDimensions += "version"
    productFlavors {
        create("demo") {
            dimension = "version"
        }
        create("full") {
            dimension = "version"
        }
    }

    flavorDimensions += "target"
    productFlavors {
        create("google") {
            dimension = "target"
        }
        create("yandex") {
            dimension = "target"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("fullGoogleRelease")
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    // Core library
    androidTestImplementation("androidx.test:core:1.5.0")

    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
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
        create<MavenPublication>("fullGoogleRelease") {
            afterEvaluate {
                from(components["fullGoogleRelease"])
            }
        }
    }
}