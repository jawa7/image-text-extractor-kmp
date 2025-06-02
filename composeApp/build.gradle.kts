import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.1.21"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        iosMain.dependencies {
            implementation(libs.ktor.darwin)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(projects.shared)
            implementation(libs.peekaboo.image.picker)
            implementation(libs.peekaboo.ui)
            implementation(libs.ktor.clientCio)
            implementation(libs.ktor.clientCore)
            implementation(libs.ktor.contentNegotiation)
            implementation(libs.ktor.clientLogging)
            implementation(libs.ktor.auth)
            implementation(libs.awssdk.s3)
            implementation(libs.kotlin.serialization)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

val properties = Properties().apply {
    load(File(rootDir, "local.properties").inputStream())
}

android {
    namespace = "com.jawa.ite"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.jawa.ite"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        println(properties["SERVER_HOST"])

        buildConfigField("String", "AUTH_PASSWORD", "\"${properties["AUTH_PASSWORD"]}\"")
        buildConfigField("String", "AUTH_USERNAME", "\"${properties["AUTH_USERNAME"]}\"")
        buildConfigField("String", "SERVER_HOST", "\"${properties["SERVER_HOST"]}\"")
    }
    packaging {
        resources {
            excludes += "/META-INF/*"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.transport.runtime)
    debugImplementation(compose.uiTooling)
}
