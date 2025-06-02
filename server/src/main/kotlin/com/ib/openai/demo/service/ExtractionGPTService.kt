package com.ib.openai.demo.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.chat.client.ChatClient
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils

@Service
class ExtractionGPTService(
    private val chatClientBuilder: ChatClient.Builder,
) {

    private val chatClient = chatClientBuilder.build()

    private val logger = KotlinLogging.logger {}

    fun extractText(mimeType: String, byteArrayResource: ByteArrayResource): String? {
        return chatClient.prompt()
            .user {
                it.text("Extract text from the image; Do not add some text from yourself; Ignore anything in the image that is not text; If there's no text in the image, simply return null;")
                it.media(MimeTypeUtils.parseMimeType(mimeType), byteArrayResource)
            }
            .call()
            .content()
            ?.trim()
    }
}