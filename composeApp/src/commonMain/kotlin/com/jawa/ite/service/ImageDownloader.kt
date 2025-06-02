package com.jawa.ite.service

import com.jawa.ite.util.AppLogger
import com.jawa.ite.util.publicHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

suspend fun downloadImageWithKtor(url: String): ByteArray? {
    return try {
        val response = publicHttpClient.get(url)
        if (response.status == HttpStatusCode.Companion.OK) {
            response.body<ByteArray>()
        } else {
            AppLogger.warn(::downloadImageWithKtor.name, "Couldn't download image, status code is ${response.status}")
            null
        }
    } catch (e: Exception) {
        AppLogger.error(::downloadImageWithKtor.name, "Failed to download image", e)
        null
    }
}
