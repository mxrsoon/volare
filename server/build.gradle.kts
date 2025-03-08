import java.io.FileInputStream
import java.util.Properties

plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
}

group = "com.mxrsoon.volare"
version = "1.0.0"

application {
    mainClass.set("com.mxrsoon.volare.ApplicationKt")

    applicationDefaultJvmArgs = listOf(
        "-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}"
    )
}

tasks {
    @Suppress("unused")
    val run by getting(JavaExec::class) {
        loadEnvFile { key, value -> environment(key, value) }
    }
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("volare-server")
        imageTag.set(version.toString())

        loadEnvFile { key, value -> environmentVariable(key, value) }
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.call.id)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.postgresql.jdbc)
    implementation(libs.h2)
    implementation(libs.logback.classic)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.kotlinx.datetime)
    implementation(libs.jbcrypt)
    implementation(libs.google.api.client)
}

private fun loadEnvFile(fileName: String = ".env", action: (String, String) -> Unit) {
    val propertiesFile = File(rootProject.rootDir, fileName)

    if (propertiesFile.exists()) {
        val properties = Properties().apply {
            load(FileInputStream(propertiesFile))
        }

        properties.forEach { key, value ->
            action(key.toString(), value.toString())
        }
    }
}