package com.ib.openai.demo.api.extract

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/extract")
class ExtractTextController(
    private val extractTextService: ExtractTextService,
) {

    @PostMapping("/text-from-image")
    fun getTextFromImage(@RequestParam fileId: String): ResponseEntity<ExtractTextResponse> {
        return ResponseEntity.ok(ExtractTextResponse(extractTextService.getText(fileId).toString()))
    }

    data class ExtractTextResponse(
        val text: String
    )
}