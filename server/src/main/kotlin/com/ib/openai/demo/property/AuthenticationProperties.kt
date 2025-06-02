package com.ib.openai.demo.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth")
data class AuthenticationProperties(
    val username: String,
    val password: String,
)
