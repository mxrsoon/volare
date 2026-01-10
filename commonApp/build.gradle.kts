import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

group = "com.mxrsoon.volare"
version = libs.versions.app.get()

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()
    
    sourceSets {
        val commonMain by getting

        val jvmAndAndroidMain by creating {
            dependsOn(commonMain)
        }

        val desktopMain by getting {
            dependsOn(jvmAndAndroidMain)
        }

        val androidMain by getting {
            dependsOn(jvmAndAndroidMain)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.material3)
            implementation(libs.material3.adaptive.navigation)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.adaptive)
            implementation(libs.androidx.adaptive.navigation)
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kermit)
            implementation(projects.shared)
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.splashscreen)
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.auth)
            implementation(libs.google.identity.googleid)
            implementation(libs.ktor.client.cio)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.cio)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        webMain.dependencies {
            implementation(libs.ktor.client.js)
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}

android {
    namespace = group.toString()
    compileSdk = libs.versions.android.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.sdk.min.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xexpect-actual-classes")
    }
}

compose {
    resources {
        packageOfResClass = "$group.resources"
    }

    desktop {
        application {
            mainClass = "${group}.MainKt"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = name
                packageVersion = version.toString()
                jvmArgs("-Dapple.awt.application.appearance=system")
            }
        }
    }
}
