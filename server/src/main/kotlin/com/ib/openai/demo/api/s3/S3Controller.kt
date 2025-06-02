package com.ib.openai.demo.api.s3

import com.ib.openai.demo.service.S3Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/s3")
class S3Controller(
    private val s3Service: S3Service
) {

    @PostMapping("/pre-signed-url")
    fun generateUrl(
        @RequestParam(name = "filename", required = true) filename: String,
    ): ResponseEntity<S3Service.PresignedUrl> {
        return ResponseEntity.ok(s3Service.generatePutPresignedUrl(filename))
    }
}
