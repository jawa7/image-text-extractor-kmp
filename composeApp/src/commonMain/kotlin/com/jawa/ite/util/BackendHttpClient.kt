package com.jawa.ite.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

expect val backendHttpClient: HttpClient

fun buildHttpClientWithAuth(
    username: String,
    password: String,
    host: String,
    timeoutMillis: Long = 10_000,
    engine: HttpClientEngineFactory<*>
): HttpClient = HttpClient(engine) {
    install(Logging) {
        level = LogLevel.INFO
    }
    install(Auth) {
        basic {
            credentials {
                BasicAuthCredentials(username, password)
            }
        }
    }
    defaultRequest {
        url(host)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = timeoutMillis
    }
}