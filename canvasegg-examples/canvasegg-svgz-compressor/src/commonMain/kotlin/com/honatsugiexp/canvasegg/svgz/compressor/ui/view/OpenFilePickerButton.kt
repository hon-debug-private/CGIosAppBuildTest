package com.honatsugiexp.canvasegg.svgz.compressor.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.uri.Uri

@Composable
expect fun OpenFilePickerButton(
    onPick: (uri: String) -> Unit,
    mimeTypes: List<String>,
    button: @Composable (onClick: () -> Unit) -> Unit
)