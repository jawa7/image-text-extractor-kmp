package com.jawa.ite.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import platform.Foundation.NSProcessInfo

actual val backendHttpClient: HttpClient = buildHttpClientWithAuth(
    username = getEnv("AUTH_USERNAME") ?: error("No username found in environments for the authentication"),
    password = getEnv("AUTH_PASSWORD") ?: error("No password found in environments for the authentication"),
    host = getEnv("SERVER_HOST") ?: error("No server host found"),
    engine = Darwin
)

private fun getEnv(key: String): String? =
    NSProcessInfo.processInfo.environment[key] as? String