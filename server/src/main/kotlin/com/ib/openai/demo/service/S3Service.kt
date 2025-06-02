package com.ib.openai.demo.service

import com.ib.openai.demo.property.S3Properties
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.InputStream
import java.time.Duration
import java.util.UUID


@Service
class S3Service(
    private val s3Properties: S3Properties,
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
) {

    fun generatePutPresignedUrl(filePath: String): PresignedUrl {
        val fileId = "/uploads/${UUID.randomUUID()}_$filePath"
        val putObjectRequestBuilder = PutObjectRequest.builder()
            .bucket(s3Properties.bucket)
            .key(fileId)
        val putObjectRequest = putObjectRequestBuilder.build()
        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(60))
            .putObjectRequest(putObjectRequest)
            .build()
        val presignedRequest = s3Presigner.presignPutObject(presignRequest)
        return PresignedUrl(fileId, presignedRequest.url().toString())
    }

    fun downloadImage(fileId: String): InputStream {
        val request = GetObjectRequest.builder()
            .bucket(s3Properties.bucket)
            .key(fileId)
            .build()
        return s3Client.getObject(request)
    }

    data class PresignedUrl(val fileId: String, val url: String)
}
