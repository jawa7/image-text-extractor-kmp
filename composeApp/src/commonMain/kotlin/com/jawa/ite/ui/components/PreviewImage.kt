package com.jawa.ite.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.pi_image_content_description
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun PreviewImage(
    image: ImageBitmap?,
    isImageSelected: Boolean,
    onImageSelectedChange: (Boolean) -> Unit
) {
    image?.let {
        LaunchedEffect(it) {
            onImageSelectedChange(false)
            delay(2000)
            onImageSelectedChange(true)
        }

        if (!isImageSelected) {
            Spacer(modifier = Modifier.height(8.dp))
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                bitmap = it,
                contentDescription = stringResource(Res.string.pi_image_content_description),
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    } ?: Spacer(modifier = Modifier.height(8.dp))
}
