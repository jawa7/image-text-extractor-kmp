package com.jawa.ite.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.etb_label
import demo.composeapp.generated.resources.etb_icon_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExtractedTextBox(extractedText: String) {
    val clipboardManager = LocalClipboardManager.current
    Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        OutlinedTextField(
            value = extractedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(Res.string.etb_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = stringResource(Res.string.etb_icon_content_description),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(PaddingValues(vertical = 12.dp, horizontal = 6.dp))
                .size(20.dp)
                .clickable {
                    clipboardManager.setText(
                        annotatedString = buildAnnotatedString {
                            append(text = extractedText)
                        }
                    )
                },
            tint = Color.Gray
        )
    }
}