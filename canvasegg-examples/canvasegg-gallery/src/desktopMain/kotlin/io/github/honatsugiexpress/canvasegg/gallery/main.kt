package io.github.honatsugiexpress.canvasegg.gallery

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.honatsugiexpress.canvasegg.gallery.ui.nav.NavInit
import io.github.honatsugiexpress.canvasegg.gallery.ui.nav.NavRoutes

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication
    ) {
        NavInit(NavRoutes.Start.name)
    }
}