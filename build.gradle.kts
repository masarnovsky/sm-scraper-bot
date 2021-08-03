import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    application
}

group = "me.masarnovsky"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jsoup:jsoup:1.11.3")
    implementation("com.github.elbekD:kt-telegram-bot:1.3.5")
    implementation("org.twitter4j:twitter4j-core:4.0.7")
    implementation("io.github.microutils:kotlin-logging:1.7.8")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}