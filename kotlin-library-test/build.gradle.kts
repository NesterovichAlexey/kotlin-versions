plugins {
    kotlin("jvm") version "1.5.32"
    application
}

group = "com.nesterovich.alexey"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.nesterovich.alexey:kotlin-library")
}

application {
    mainClass.set("MainKt")
}