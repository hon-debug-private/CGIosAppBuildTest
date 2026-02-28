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
        namespace = "io.github.honatsugiexpress.canvasegg.svgz"
        compileSdk = 36
        minSdk = 23

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
        iosX64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CanvasEggSvgz"
            isStatic = true
        }
    }

    jvm("desktop")

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
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
        val desktopMain by getting
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.compose.components.resources)
                implementation(libs.ksoup)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.okio)
                implementation(project(":canvasegg-core"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependsOn(jvmMain.get())
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        desktopMain.dependsOn(jvmMain.get())
        desktopMain.dependencies {
            implementation(libs.kotlin.multiplatform.appdirs)
        }
        jvmMain {
            dependsOn(commonMain.get())
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.junit)
            }
        }

        iosMain {
            dependsOn(commonMain.get())
            dependsOn(nativeMain.get())
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }

        iosX64Main {
            dependsOn(iosMain.get())
        }
        iosArm64Main {
            dependsOn(iosMain.get())
        }
        iosSimulatorArm64Main {
            dependsOn(iosMain.get())
        }
        jsMain {
            dependencies {
                implementation(libs.kotlinx.browser)
                implementation(npm("pako", "2.1.0"))
            }
        }
        wasmJsMain {
            dependencies {
                implementation(libs.kotlinx.browser)
                implementation(npm("pako", "2.1.0"))
            }
        }
    }

}


publishing {
    repositories {
        mavenLocal()
    }
}