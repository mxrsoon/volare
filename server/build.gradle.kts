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
    val run by getting(JavaExec::class) {
        val localProperties = Properties().apply {
            load(FileInputStream(File(rootProject.rootDir, "local.properties")))
        }

        environment("DB_URL", localProperties.getProperty("db.url"))
        environment("DB_USER", localProperties.getProperty("db.user"))
        environment("DB_PASSWORD", localProperties.getProperty("db.password"))
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
    implementation(libs.postgresql.jdbc)
    implementation(libs.h2)
    implementation(libs.logback.classic)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.kotlinx.datetime)
    implementation(libs.jbcrypt)
}
