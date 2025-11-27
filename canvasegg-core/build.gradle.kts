@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    androidLibrary {
        namespace = "com.honatsugiexp.canvasegg"
        compileSdk = 36
        minSdk = 21
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
        iosX64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CanvasEgg"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
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
        androidMain {
            dependsOn(jvmMain.get())
            dependencies {
                implementation(libs.androidx.customview.poolingcontainer)
                implementation(compose.uiTooling)
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.emoji2.text)
            }
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(libs.ksoup)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(project(":canvasegg-common"))
            implementation(project(":canvasegg-css"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(compose.uiTest)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        desktopMain.dependsOn(jvmMain.get())
        desktopMain.dependsOn(notAndroidMain)
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
        jvmMain {
            dependencies {
                implementation(libs.jstyleparser)
            }
        }
        wasmJsMain {
            dependsOn(webMain.get())
        }
        jsMain {
            dependsOn(webMain.get())
        }
        webMain {
            dependsOn(notAndroidMain)
        }
        notAndroidMain.dependsOn(commonMain.get())
    }
}