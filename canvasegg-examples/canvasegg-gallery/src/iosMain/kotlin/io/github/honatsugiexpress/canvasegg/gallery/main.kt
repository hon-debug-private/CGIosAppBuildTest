package io.github.honatsugiexpress.canvasegg.gallery

import androidx.compose.ui.window.ComposeUIViewController
import io.github.honatsugiexpress.canvasegg.gallery.ui.nav.NavInit
import io.github.honatsugiexpress.canvasegg.gallery.ui.nav.NavRoutes
import io.github.honatsugiexpress.canvasegg.gallery.ui.theme.CanvasEggGalleryTheme

@Suppress("unused", "FunctionName")
fun MainViewController() = ComposeUIViewController {
    CanvasEggGalleryTheme {
        NavInit(NavRoutes.Start.name)
    }
}