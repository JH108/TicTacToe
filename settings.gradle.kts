enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

//plugins {
//    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
//}

rootProject.name = "TicTacToe"
include(":androidApp")
include(":shared")