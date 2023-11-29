plugins {
    kotlin("multiplatform") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.20"
    id("app.cash.sqldelight") version "2.0.0"
    application
}

group = "me.jesse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
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
        val commonMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
                implementation("app.cash.sqldelight:primitive-adapters:2.0.0")
                implementation("com.benasher44:uuid:0.8.2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
                implementation("io.ktor:ktor-server-core-jvm:2.3.2")
                implementation("io.ktor:ktor-server-resources:2.3.2")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
                implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.2")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
                implementation("io.ktor:ktor-server-netty:2.3.2")
                implementation("io.ktor:ktor-server-html-builder-jvm:2.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.346")
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("me.jesse.application.ServerKt")
}

sqldelight {
    databases {
        create("TicTacToeDatabase") {
            packageName.set("me.jesse.database")
        }
    }
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}