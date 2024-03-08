plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "1.9.20"
    id("app.cash.sqldelight") version "2.0.0"
//    application
}

group = "me.jessehill"
version = "1.2-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

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
//        withJava()
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
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        jvmMain.dependencies {
            implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
            implementation("io.ktor:ktor-server-core-jvm:2.3.2")
            implementation("io.ktor:ktor-server-resources:2.3.2")
            implementation("io.ktor:ktor-server-call-logging-jvm:2.3.2")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
            implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.2")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
            implementation("io.ktor:ktor-server-netty:2.3.2")
            implementation("io.ktor:ktor-server-html-builder-jvm:2.3.2")
            implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
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

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

//application {
//    mainClass.set("me.jessehill.application.ServerKt")
//}
//
//sqldelight {
//    databases {
//        create("TicTacToeDatabase") {
//            packageName.set("me.jessehill.database")
//        }
//    }
//}

//tasks.named<Copy>("jvmProcessResources") {
//    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
//    from(jsBrowserDistribution)
//}
//
//tasks.named<JavaExec>("run") {
//    dependsOn(tasks.named<Jar>("jvmJar"))
//    classpath(tasks.named<Jar>("jvmJar"))
//}

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
