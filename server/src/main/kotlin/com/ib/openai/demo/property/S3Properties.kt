package com.ib.openai.demo.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "s3")
data class S3Properties(
    val accessKey: String,
    val secretKey: String,
    val region: String,
    val bucket: String,
)