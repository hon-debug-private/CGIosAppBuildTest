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
        namespace = "io.github.honatsugiexpress.canvasegg.resolvers.file"
        compileSdk = 36
        minSdk = 23

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
            baseName = "CanvasEggResolversFile"
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
        val notAndroidMain by creating
        val notIosMain by creating
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.compose.components.resources)
                implementation(libs.okio)
                implementation(project(":canvasegg-core"))
                implementation(project(":canvasegg-common"))
                // Add KMP dependencies here
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependsOn(notIosMain)
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
                implementation(libs.androidx.customview.poolingcontainer)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.emoji2.text)
                implementation(libs.androidx.documentfile)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.test.core)
                implementation(libs.androidx.junit)
            }
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        desktopMain.dependsOn(jvmMain.get())
        desktopMain.dependsOn(notAndroidMain)
        desktopMain.dependsOn(notIosMain)
        iosMain {
            dependsOn(commonMain.get())
            dependsOn(nativeMain.get())
            dependsOn(notAndroidMain)
        }
        iosArm64Main {
            dependsOn(iosMain.get())
        }
        iosSimulatorArm64Main {
            dependsOn(iosMain.get())
        }
        iosX64Main {
            dependsOn(iosMain.get())
        }
        wasmJsMain {
            dependsOn(webMain.get())
        }
        jsMain {
            dependsOn(webMain.get())
        }
        webMain {
            dependsOn(notAndroidMain)
            dependsOn(notIosMain)
        }
        notAndroidMain.dependsOn(commonMain.get())
        notIosMain.dependsOn(commonMain.get())
    }

}


publishing {
    repositories {
        mavenLocal()
    }
}