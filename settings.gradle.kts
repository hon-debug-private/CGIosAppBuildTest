rootProject.name = "CanvasEgg"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":canvasegg-core")
include(":canvasegg-css")
include(":canvasegg-common")
include(":canvasegg-examples:canvasegg-gallery")
include(":canvasegg-examples:canvasegg-gallery-app")
include(":canvasegg-examples:canvasegg-svg-test-suite")
include(":canvasegg-extendable")
include(":canvasegg-pdf")
include(":canvasegg-imageio")
include(":canvasegg-svgz")
include(":canvasegg-examples:canvasegg-svgz-compressor")
