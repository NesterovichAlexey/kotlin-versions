plugins {
    id("com.android.library") version "7.4.2"
    kotlin("android") version "1.5.32"
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
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.nesterovich.alexey:android-library")
}
