package com.jawa.ite.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jawa.ite.ui.components.ExtractedTextBox
import com.jawa.ite.ui.components.ImageInputSelector
import com.jawa.ite.ui.components.PreviewImage
import com.jawa.ite.ui.components.TextExtractActionButton
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.ms_additional_title
import demo.composeapp.generated.resources.ms_title
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalUuidApi::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()

    var inputMethod by remember { mutableStateOf("upload") }
    var imageUrl by remember { mutableStateOf("") }
    var extractedText by remember { mutableStateOf<String?>(null) }
    var imageSelected by remember { mutableStateOf(false) }


    var imageToPreview by remember { mutableStateOf<ImageBitmap?>(null) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(Res.string.ms_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    stringResource(Res.string.ms_additional_title),
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                ImageInputSelector(
                    inputMethod = inputMethod,
                    setInputMethod = { inputMethod = it },
                    imageUrl = imageUrl,
                    setImageToPreview = { imageToPreview = it },
                    setImageByteArray = { imageByteArray = it },
                    onUrlChange = { imageUrl = it },
                    scope = scope,
                )

                PreviewImage(
                    imageToPreview,
                    imageSelected,
                    { imageSelected = it }
                )

                TextExtractActionButton(
                    scope = scope,
                    imageSelected = imageSelected,
                    imageUrl = imageUrl,
                    imageByteArray = imageByteArray,
                    setExtractedText = { extractedText = it }
                )

                extractedText?.let {
                    ExtractedTextBox(extractedText = it)
                }
            }
        }
    }
}
