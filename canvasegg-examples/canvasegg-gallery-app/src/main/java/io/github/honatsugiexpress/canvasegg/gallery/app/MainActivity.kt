package io.github.honatsugiexpress.canvasegg.gallery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.honatsugiexpress.canvasegg.gallery.ui.nav.NavInit
import io.github.honatsugiexpress.canvasegg.gallery.ui.nav.NavRoutes
import io.github.honatsugiexpress.canvasegg.gallery.ui.theme.CanvasEggGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasEggGalleryTheme {
                NavInit(NavRoutes.Start.name)
            }
        }
    }
}