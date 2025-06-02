package com.ib.openai.demo.api.extract

interface ExtractTextService {

    fun getText(fileId: String): String?
}