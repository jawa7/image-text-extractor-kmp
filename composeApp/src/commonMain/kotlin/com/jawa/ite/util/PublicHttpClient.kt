package com.jawa.ite.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

expect val publicHttpClient: HttpClient

fun buildHttpClient(timeoutMillis: Long = 10_000): HttpClient = HttpClient(CIO) {
    install(Logging) {
        level = LogLevel.INFO
    }
    install(HttpTimeout) {
        requestTimeoutMillis = timeoutMillis
    }
}
