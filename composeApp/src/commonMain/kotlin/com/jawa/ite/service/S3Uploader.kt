package com.jawa.ite.service

import com.jawa.ite.util.AppLogger
import com.jawa.ite.util.backendHttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private suspend fun getPresignedUrl(fileName: String): PresignedUrl? =
    runCatching {
        val response = backendHttpClient.post("/s3/pre-signed-url") {
            contentType(ContentType.Application.Json)
            parameter("filename", fileName)
        }

        AppLogger.info(::getPresignedUrl.name, "Pre-signed URL response status: ${response.status}")
        val responseBody = response.bodyAsText()

        if (response.status == HttpStatusCode.OK) {
            Json.decodeFromString<PresignedUrl>(responseBody)
        } else {
            AppLogger.warn(::getPresignedUrl.name, "Failed to get presigned URL, status code is ${response.status}")
            null
        }
    }.getOrElse {
        AppLogger.error(::getPresignedUrl.name, "Exception during getPresignedUrl", it)
        null
    }

suspend fun uploadImage(bytes: ByteArray, filename: String): String? =
    getPresignedUrl(filename)?.let { presignedUrl ->
        val response = backendHttpClient.put(presignedUrl.url) {
            setBody(bytes)
        }
        AppLogger.info(::uploadImage.name, "Upload response status: ${response.status}")
        if (response.status == HttpStatusCode.OK) {
            presignedUrl.fileId
        } else {
            AppLogger.info(::uploadImage.name, "Image upload failed, status code is ${response.status}")
            null
        }
    }

@Serializable
data class PresignedUrl(val fileId: String, val url: String)
