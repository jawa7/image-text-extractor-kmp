package com.ib.openai.demo.configuration

import com.ib.openai.demo.property.S3Properties

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider

@Configuration
class S3Config(
    private val s3Properties: S3Properties
) {
    @Bean
    fun s3Client(): S3Client {

        val awsRegion: Region = Region.of(s3Properties.region)
        val awsCredentials: AwsBasicCredentials =
            AwsBasicCredentials.create(s3Properties.accessKey, s3Properties.secretKey)
        return S3Client.builder()
            .region(awsRegion)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        val awsRegion: Region = Region.of(s3Properties.region)
        return S3Presigner.builder()
            .region(awsRegion)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        s3Properties.accessKey,
                        s3Properties.secretKey
                    )
                )
            )
            .build()
    }
}
