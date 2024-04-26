plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "1.9.20"
    id("app.cash.sqldelight") version "2.0.0"
}

group = "me.jessehill"
version = "1.2-SNAPSHOT"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    jvm {
        jvmToolchain(17)

        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }

    js {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            implementation("app.cash.sqldelight:primitive-adapters:2.0.0")
            implementation("com.benasher44:uuid:0.8.2")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.content.negotiation)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation("app.cash.sqldelight:android-driver:2.0.1")
            implementation(libs.ktor.android.client)
        }

        jvmMain.dependencies {
            implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
            implementation(libs.ktor.server.core.jvm)
            implementation(libs.ktor.server.resources)
            implementation(libs.ktor.server.call.logging.jvm)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.server.content.negotiation.jvm)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.server.netty)
            implementation(libs.ktor.server.html.builder.jvm)
            implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.1")
        }

        jsMain.dependencies {
            // React, React DOM + Wrappers
            implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.430"))
            implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
            implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
            // Kotlin React Router DOM
            implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom")

            // Kotlin React Emotion (CSS)
            implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")

            // Coroutines & serialization
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
        }

        iosMain.dependencies {
            implementation("app.cash.sqldelight:native-driver:2.0.1")
            implementation(libs.ktor.ios.client)
        }
    }
}

sqldelight {
    databases {
        create("TicTacToeDatabase") {
            packageName.set("me.jessehill.database")
        }
    }
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

android {
    namespace = "me.jessehill"
    compileSdk = 34
    defaultConfig {
        minSdk = 31
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
