import com.apollographql.apollo.gradle.ApolloExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.apollographql.apollo:apollo-gradle-plugin:1.0.0-alpha5")
    }
}

apply(plugin = "com.apollographql.android")

group = "repo_stats"
version = "1.0-SNAPSHOT"

repositories {
    google()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.21")

    implementation("com.apollographql.apollo:apollo-runtime:1.0.0-alpha5")

    implementation("com.squareup.okhttp3:okhttp:3.14.0")
    implementation("com.google.code.gson:gson:2.8.5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

configure<ApolloExtension> {
    setCustomTypeMapping(
        mapOf(
            "URI" to "java.lang.String",
            "DATETIME" to "java.time.ZonedDateTime"
        )
    )
}
