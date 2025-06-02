package com.jawa.ite.service

import com.jawa.ite.util.AppLogger
import com.jawa.ite.util.backendHttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun getTextFromImage(fileId: String): String? {
    return try {
        val response = backendHttpClient.post("/api/extract/text-from-image") {
            parameter("fileId", fileId)
            contentType(ContentType.Application.Json)
        }

        AppLogger.info(::getTextFromImage.name, "Text extractor response status: ${response.status}")
        val responseBody = response.bodyAsText()

        if (response.status != HttpStatusCode.OK) {
            AppLogger.warn(::getTextFromImage.name, "Failed to get text, status code is ${response.status}")
            return null
        }

        val text = Json.decodeFromString<TextFromImageResponse>(responseBody).text
        if ("null" in text) "No text on the image" else text
    } catch (e: Exception) {
        AppLogger.error(::getTextFromImage.name, "Exception during extracting text", e)
        return null
    }
}

@Serializable
data class TextFromImageResponse(
    val text: String
)