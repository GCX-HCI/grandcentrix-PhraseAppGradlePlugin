import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath("guru.stefma.bintrayrelease:bintrayrelease:1.1.2")
    }
}

plugins {
    kotlin("jvm") version "1.3.31"
    `java-gradle-plugin`
    `maven-publish`
}
apply(plugin = "guru.stefma.bintrayrelease")

group = "net.grandcentrix"
version = "1.0.0"
configure<guru.stefma.androidartifacts.ArtifactsExtension> {
    artifactId = "PhraseAppGradlePlugin"
}
configure<guru.stefma.bintrayrelease.PublishExtension> {
    userOrg = "grandcentrix"
    uploadName = "PhraseAppGradlePlugin"
    desc = "A Gradle plugin to receive strings from PhraseApp"
    website = "https://github.com/grandcentrix/PhraseAppGradlePlugin"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
}

gradlePlugin {
    plugins {
        create("phraseapp") {
            id = "net.grandcentrix.phraseapp"
            implementationClass = "net.grandcentrix.phraseapp.PhraseAppPlugin"
        }
    }
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>().all {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()

    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}