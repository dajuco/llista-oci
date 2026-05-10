plugins {
    kotlin("multiplatform") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
    id("com.android.application") version "8.2.2"
    id("org.jetbrains.compose") version "1.6.10"
    id("org.jetbrains.dokka") version "1.9.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

kotlin {
    androidTarget()
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                implementation(kotlin("reflect"))
            }
        }
        val androidMain by getting {
            kotlin.srcDir("src/main/kotlin")
            kotlin.srcDir("src/androidMain/kotlin")
            dependencies {
                implementation("androidx.activity:activity-compose:1.9.0")
            }
        }
        val desktopMain by getting {
            kotlin.srcDir("src/main/kotlin")
            kotlin.srcDir("src/desktopMain/kotlin")
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }

    jvmToolchain(17)
}

android {
    namespace = "org.example.llistaoci"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.example.llistaoci"
        minSdk = 24
    }

    sourceSets["main"].assets.srcDirs("src/androidMain/assets")
}

androidComponents {
    beforeVariants(selector().withBuildType("release")) { variantBuilder ->
        variantBuilder.enable = false
    }
}

compose.desktop {
    application {
        mainClass = "DesktopMainKt"
    }
}
