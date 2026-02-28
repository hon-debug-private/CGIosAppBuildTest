import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    `maven-publish`
}

group = "io.github.honatsugiexpress.canvasegg"
version = libs.versions.canvaseggCore.get()

kotlin {
    android {
        namespace = "io.github.honatsugiexpress.canvasegg"
        compileSdk = 36
        minSdk = 23
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
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
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
        val desktopTest by getting
        val notAndroidMain by creating
        androidMain {
            dependsOn(jvmMain.get())
            dependencies {
                implementation(libs.androidx.customview.poolingcontainer)
                implementation(libs.compose.ui.tooling)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.emoji2.text)
            }
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.ksoup)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.antlr.kotlin.runtime)
            implementation(project(":canvasegg-common"))
            implementation(project(":canvasegg-css"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
        desktopMain.dependsOn(jvmMain.get())
        desktopMain.dependsOn(notAndroidMain)

        desktopTest.dependencies {
            implementation(project(":canvasegg-resolvers-file"))
        }
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
        }
        notAndroidMain.dependsOn(commonMain.get())
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}