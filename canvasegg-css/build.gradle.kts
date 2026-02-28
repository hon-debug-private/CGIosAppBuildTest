@file:OptIn(ExperimentalWasmDsl::class)

import com.strumenta.antlrkotlin.gradle.AntlrKotlinTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.antlrKotlin)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    `maven-publish`
}

val generateKotlinGrammarSource = tasks.register<AntlrKotlinTask>("generateKotlinGrammarSource") {
    dependsOn("cleanGenerateKotlinGrammarSource")

    source = fileTree(layout.projectDirectory.dir("antlr")) {
        include("**/*.g4")
    }

    val pkgName = "io.github.honatsugiexpress.cssparser.antlr"
    packageName = pkgName

    arguments = listOf("-visitor")

    val outDir = "generatedAntlr/${pkgName.replace(".", "/")}"
    outputDirectory = layout.buildDirectory.dir(outDir).get().asFile
}

group = "io.github.honatsugiexpress.canvasegg"
version = libs.versions.canvaseggCore.get()

kotlin {
    androidLibrary {
        namespace = "io.github.honatsugiexpress.cssparser"
        compileSdk = 36
        minSdk = 23
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

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "CssParserKit"
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
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
                implementation(libs.compose.ui)
                implementation(libs.kotlin.stdlib)
                implementation(libs.ksoup)
                implementation(libs.antlr.kotlin.runtime)
                implementation(libs.kotlinx.serialization.json)
                implementation(project(":canvasegg-common"))
            }
            kotlin {
                srcDir(generateKotlinGrammarSource)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
            }
        }

        iosMain {
            dependencies {

            }
        }
    }

}


publishing {
    repositories {
        mavenLocal()
    }
}