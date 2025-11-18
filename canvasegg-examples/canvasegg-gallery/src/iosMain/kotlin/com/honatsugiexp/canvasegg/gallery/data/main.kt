package com.honatsugiexp.canvasegg.gallery.data

import androidx.compose.ui.window.ComposeUIViewController
import com.honatsugiexp.canvasegg.gallery.ui.nav.NavInit
import com.honatsugiexp.canvasegg.gallery.ui.nav.NavRoutes
import com.honatsugiexp.canvasegg.gallery.ui.theme.CanvasEggGalleryTheme

@Suppress("unused", "FunctionName")
fun MainViewController() = ComposeUIViewController {
    CanvasEggGalleryTheme {
        NavInit(NavRoutes.Start.name)
    }
}