package com.honatsugiexp.canvasegg.gallery

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.honatsugiexp.canvasegg.gallery.ui.nav.NavInit
import com.honatsugiexp.canvasegg.gallery.ui.nav.NavRoutes

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication
    ) {
        NavInit(NavRoutes.Start.name)
    }
}