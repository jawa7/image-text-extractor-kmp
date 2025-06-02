package com.jawa.ite.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import com.jawa.ite.BuildConfig

actual val backendHttpClient: HttpClient = buildHttpClientWithAuth(
    username = BuildConfig.AUTH_USERNAME.also { AppLogger.info(::backendHttpClient.name, " something " + BuildConfig.AUTH_USERNAME) },
    password = BuildConfig.AUTH_PASSWORD,
    host = BuildConfig.SERVER_HOST,
    engine = CIO
)
