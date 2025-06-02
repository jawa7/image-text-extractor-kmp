plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    kotlin("plugin.spring") version "1.9.25"
    application
}

group = "com.ib.openai"
version = "0.0.3-SNAPSHOT"

application {
    mainClass.set("com.jawa.ite.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.springBoot.web)
    implementation(libs.springBoot.redis)
    implementation(libs.springBoot.security)
    implementation(libs.springBoot.ai)

    implementation(libs.awssdk.s3)

    implementation(libs.jackson)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlin.logging)

    testImplementation(libs.springBoot.test)
    testImplementation(libs.springBoot.testcontainers)
    implementation(projects.shared)
    testImplementation(libs.kotlin.testJunit)
}


extra["springAiVersion"] = "1.0.0-M6"

dependencyManagement {
    imports {
        mavenBom(libs.springAi.bom.get().toString())
    }
}