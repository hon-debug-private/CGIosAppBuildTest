@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    androidLibrary {
        namespace = "io.github.honatsugiexpress.cssparser"
        compileSdk = 36
        minSdk = 23
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    jvm("desktop")

    js {
        browser()
    }

    wasmJs {
        browser()
    }

    val xcfName = "CanvasEggGalleryKit"
    val xcf = XCFramework(xcfName)
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries {
            framework {
                baseName = xcfName
                xcf.add(this)
            }
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        all {
            languageSettings {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.navigation.compose)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.compose.material3.window.size.class1)
                implementation(libs.ksoup)
                implementation(project(":canvasegg-core"))

                // Add KMP dependencies here
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.customview.poolingcontainer)
                implementation(libs.compose.ui.tooling)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.core.runtime)
                implementation(libs.androidx.core)
                implementation(libs.androidx.emoji2.text)
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.runner)
            implementation(project(":canvasegg-core"))
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}

compose.desktop {
    application {
        mainClass = "io.github.honatsugiexpress.canvasegg.gallery.MainKt"
        nativeDistributions {
            targetFormats(
                TargetFormat.Exe,
                TargetFormat.Dmg,
                TargetFormat.Deb,
                TargetFormat.Rpm,
                TargetFormat.AppImage
            )
            packageName = "io.github.honatsugiexpress.canvasegg.gallery"
            packageVersion = "1.0.0"
        }
    }
}