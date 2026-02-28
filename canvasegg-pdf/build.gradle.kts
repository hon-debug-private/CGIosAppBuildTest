@file:OptIn(ExperimentalWasmDsl::class)
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    `maven-publish`
}

group = "io.github.honatsugiexpress.canvasegg"
version = libs.versions.canvaseggCore.get()

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "io.github.honatsugiexpress.canvasegg.pdf"
        compileSdk = 36
        minSdk = 23

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    jvm("desktop")

    js {
        browser()
    }

    wasmJs {
        browser()
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "CanvasEggPdf"
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.compilations.getByName("main") {
            val coreGraphics by cinterops.creating {
                packageName("coregraphics")
            }
        }
        target.binaries {
            framework {
                baseName = xcfName
            }
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        val desktopMain by getting
        val desktopTest by getting
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.ksoup)
                implementation(libs.okio)
                implementation(project(":canvasegg-core"))
                implementation(project(":canvasegg-extendable"))
                implementation(project(":canvasegg-css"))
                // Add KMP dependencies here
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.compose.ui.test)
            }
        }

        desktopTest.dependencies {
            implementation(libs.kotlin.multiplatform.appdirs)
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.

            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.junit)
            }
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
        desktopMain.dependencies {
            implementation(libs.pdfbox)
        }
        jsMain {
            dependencies {
                implementation(npm("jspdf", "3.0.4"))
            }
        }
        wasmJsMain {
            dependencies {
                implementation(npm("jspdf", "3.0.4"))
            }
        }
    }

}

publishing {
    repositories {
        mavenLocal()
    }
}