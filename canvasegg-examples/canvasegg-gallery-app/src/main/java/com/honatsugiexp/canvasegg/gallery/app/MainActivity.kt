package com.honatsugiexp.canvasegg.gallery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.honatsugiexp.canvasegg.gallery.ui.nav.NavInit
import com.honatsugiexp.canvasegg.gallery.ui.nav.NavRoutes
import com.honatsugiexp.canvasegg.gallery.ui.theme.CanvasEggGalleryTheme

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