package io.github.honatsugiexpress.canvasegg.svgz.compressor

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication
    ) {

    }
}