package com.jawa.ite.util

import io.ktor.client.HttpClient

actual val publicHttpClient: HttpClient = buildHttpClient()