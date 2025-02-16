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

val localPropertiesMap = mapOf(
    "db.url" to "DB_URL",
    "db.user" to "DB_USER",
    "db.password" to "DB_PASSWORD",
    "jwt.secret" to "JWT_SECRET",
    "jwt.issuer" to "JWT_ISSUER",
    "jwt.audience" to "JWT_AUDIENCE",
    "jwt.realm" to "JWT_REALM",
    "oauth.google.client-id" to "OAUTH_GOOGLE_CLIENT_ID",
    "oauth.google.secret" to "OAUTH_GOOGLE_CLIENT_SECRET"
)

application {
    mainClass.set("com.mxrsoon.volare.ApplicationKt")

    applicationDefaultJvmArgs = listOf(
        "-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}"
    )
}

tasks {
    val run by getting(JavaExec::class) {
        val propertiesFile = File(rootProject.rootDir, "local.properties")

        if (propertiesFile.exists()) {
            val properties = Properties().apply {
                load(FileInputStream(propertiesFile))
            }

            localPropertiesMap.forEach { (propKey, envKey) ->
                properties.getProperty(propKey)?.let { environment(envKey, it) }
            }
        }
    }
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("volare-server")
        imageTag.set(version.toString())

        val propertiesFile = File(rootProject.rootDir, "local.properties")

        if (propertiesFile.exists()) {
            val properties = Properties().apply {
                load(FileInputStream(propertiesFile))
            }

            localPropertiesMap.forEach { (propKey, envKey) ->
                properties.getProperty(propKey)?.let { environmentVariable(envKey, it) }
            }

            localPropertiesMap.forEach { (propKey, envKey) ->
                properties.getProperty("docker.$propKey")?.let { environmentVariable(envKey, it) }
            }
        }
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
    implementation(libs.google.api.client)
}
