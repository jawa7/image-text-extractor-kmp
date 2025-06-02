package com.ib.openai.demo.api.extract

import com.ib.openai.demo.service.ExtractionGPTService
import com.ib.openai.demo.service.S3Service
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import java.net.URLConnection

@Service
class ExtractTextServiceImpl(
    private val s3Service: S3Service,
    private val extractionGPTService: ExtractionGPTService,
): ExtractTextService {

    private val logger = KotlinLogging.logger {}

    override fun getText(fileId: String): String? {
        val imageInputStream = s3Service.downloadImage(fileId)
        val bytes = imageInputStream.readBytes()
        val mimeType = detectMimeType(fileId, bytes)
        val byteArrayResource = ByteArrayResource(bytes)

        val text = extractionGPTService.extractText(mimeType, byteArrayResource)
        println(text)
        return text
    }

    private fun detectMimeType(fileName: String, content: ByteArray): String {
        return when {
            fileName.endsWith(".png", ignoreCase = true) -> "image/png"
            fileName.endsWith(".jpg", ignoreCase = true) || fileName.endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
            fileName.endsWith(".webp", ignoreCase = true) -> "image/webp"
            else -> URLConnection.guessContentTypeFromStream(content.inputStream()) ?: "application/octet-stream"
        }
    }
}