plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.mxrsoon.volare"
version = "1.0.0"

application {
    mainClass.set("com.mxrsoon.volare.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
}