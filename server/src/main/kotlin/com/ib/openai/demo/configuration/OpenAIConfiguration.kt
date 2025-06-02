package com.ib.openai.demo.configuration

import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAIConfiguration {

    @Bean
    fun openAiChatClient(chatClientBuilder: ChatClient.Builder): ChatClient =
        chatClientBuilder.build()
}
