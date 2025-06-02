package com.jawa.ite.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.iis_button_choose_image
import demo.composeapp.generated.resources.iis_row_enter_image_url_label
import demo.composeapp.generated.resources.iis_row_upload_image_label
import demo.composeapp.generated.resources.iis_textfield_icon_content_description_invalid
import demo.composeapp.generated.resources.iis_textfield_icon_content_description_valid
import demo.composeapp.generated.resources.iis_textfield_label
import demo.composeapp.generated.resources.iis_textfield_placeholder
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.stringResource
import kotlin.ByteArray

@Composable
fun ImageInputSelector(
    inputMethod: String,
    imageUrl: String = "",
    setImageToPreview: (androidx.compose.ui.graphics.ImageBitmap) -> Unit,
    setImageByteArray: (ByteArray) -> Unit,
    setInputMethod: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    scope: CoroutineScope
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        listOf(
            "upload" to stringResource(Res.string.iis_row_upload_image_label),
            "url" to stringResource(Res.string.iis_row_enter_image_url_label)
        ).forEach { (value, label) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { setInputMethod(value) }
            ) {
                RadioButton(
                    selected = inputMethod == value,
                    onClick = { setInputMethod(value) },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Blue)
                )
                Text(
                    text = label,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (inputMethod == "upload") {
        val singleImagePicker = rememberImagePickerLauncher(
            selectionMode = SelectionMode.Single,
            scope = scope,
            onResult = { byteArrays ->
                val byteArray = byteArrays.firstOrNull()
                checkNotNull(byteArray)
                setImageToPreview(byteArray.toImageBitmap())
                setImageByteArray(byteArray)
            },
        )

        Button(onClick = { singleImagePicker.launch() }) {
            Text(stringResource(Res.string.iis_button_choose_image))
        }
    } else {
        val isValid = remember(imageUrl) {
            imageUrl.trim().lowercase().let {
                it.endsWith(".jpg") || it.endsWith(".jpeg") || it.endsWith(".png") || it.endsWith(".webp")
            }
        }

        OutlinedTextField(
            value = imageUrl,
            onValueChange = onUrlChange,
            label = { Text(stringResource(Res.string.iis_textfield_label)) },
            placeholder = { Text(stringResource(Res.string.iis_textfield_placeholder)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                if (imageUrl.isNotBlank()) {
                    Icon(
                        imageVector = if (isValid) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = if (isValid) stringResource(Res.string.iis_textfield_icon_content_description_valid) else stringResource(Res.string.iis_textfield_icon_content_description_invalid),
                        tint = if (isValid) Color(0xFF4CAF50) else Color.Red
                    )
                }
            },
            isError = imageUrl.isNotBlank() && !isValid
        )
    }
}