package io.github.honatsugiexpress.canvasegg.svgz.compressor.ui.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun OpenFilePickerButton(
    onPick: (uri: String) -> Unit,
    mimeTypes: List<String>,
    button: @Composable (onClick: () -> Unit) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            onPick(uri.toString())
        }
    }
    button {
        launcher.launch(mimeTypes.toTypedArray())
    }
}