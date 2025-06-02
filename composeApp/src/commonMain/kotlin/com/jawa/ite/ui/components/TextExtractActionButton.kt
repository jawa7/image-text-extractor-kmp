package com.jawa.ite.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jawa.ite.service.downloadImageWithKtor
import com.jawa.ite.service.getTextFromImage
import com.jawa.ite.service.uploadImage
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.teab_button_text
import demo.composeapp.generated.resources.teab_extracted_text_error
import demo.composeapp.generated.resources.teab_image_failed
import demo.composeapp.generated.resources.teab_something_went_wrong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun TextExtractActionButton(
    scope: CoroutineScope,
    imageSelected: Boolean,
    imageUrl: String?,
    imageByteArray: ByteArray?,
    setExtractedText: (String) -> Unit,
) {

    var isLoading by remember { mutableStateOf(false) }

    val errorImageFailed = stringResource(Res.string.teab_image_failed)
    val errorUnknown = stringResource(Res.string.teab_something_went_wrong)
    val errorExtractedText = stringResource(Res.string.teab_extracted_text_error)

    when {
        !isLoading && !imageSelected && imageUrl.isNullOrEmpty() -> {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(stringResource(Res.string.teab_button_text))
            }
        }

        !isLoading && imageSelected && !imageUrl.isNullOrEmpty() -> {
            Button(onClick = {
                extractTextFromUrlImage(
                    scope = scope,
                    imageUrl = imageUrl,
                    setIsLoading = { isLoading = it },
                    setExtractedText = setExtractedText,
                    errorImageFailed = errorImageFailed,
                    errorUnknown = errorUnknown,
                    errorExtractedText = errorExtractedText,
                )
            }) {
                Text(stringResource(Res.string.teab_button_text))
            }
        }

        !isLoading && imageSelected && imageByteArray != null -> {
            Button(onClick = {
                extractTextFromUploadedImage(
                    scope = scope,
                    setIsLoading = { isLoading = it },
                    setExtractedText = setExtractedText,
                    imageByteArray = imageByteArray,
                    errorUnknown = errorUnknown,
                    errorExtractedText = errorExtractedText
                )
            }) {
                Text(stringResource(Res.string.teab_button_text))
            }
        }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }
}

@OptIn(ExperimentalUuidApi::class)
fun extractTextFromUrlImage(
    scope: CoroutineScope,
    setIsLoading: (Boolean) -> Unit,
    setExtractedText: (String) -> Unit,
    imageUrl: String,
    errorImageFailed: String,
    errorUnknown: String,
    errorExtractedText: String
) {
    scope.launch {
        setIsLoading(true)
        val result = runCatching { downloadImageWithKtor(imageUrl) }.getOrNull()
        if (result == null) {
            setExtractedText(errorImageFailed)
            setIsLoading(false)
            return@launch
        }
        val fileId = uploadImage(result, Uuid.random().toString())
        if (fileId != null) {
            val text = getTextFromImage(fileId)
            if (text == null) {
                setExtractedText(errorExtractedText)
            } else {
                setExtractedText(text)
            }
        } else {
            errorUnknown
        }
        setIsLoading(false)
    }
}

@OptIn(ExperimentalUuidApi::class)
fun extractTextFromUploadedImage(
    scope: CoroutineScope,
    setIsLoading: (Boolean) -> Unit,
    setExtractedText: (String) -> Unit,
    imageByteArray: ByteArray,
    errorUnknown: String,
    errorExtractedText: String
) {
    scope.launch {
        setIsLoading(true)
        val fileId = uploadImage(imageByteArray, Uuid.random().toString())
        if (fileId != null) {
            val text = getTextFromImage(fileId)
            if (text == null) {
                setExtractedText(errorExtractedText)
            } else {
                setExtractedText(text)
            }
        } else {
            errorUnknown
        }
        setIsLoading(false)
    }
}