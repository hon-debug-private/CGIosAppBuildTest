package io.github.honatsugiexpress.canvasegg.gallery.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.honatsugiexpress.canvasegg.gallery.data.GalleryDatas
import io.github.honatsugiexpress.canvasegg.gallery.ui.view.GalleryCard

@Composable
fun GalleryScreen() {
    LaunchedEffect(Unit) {
        GalleryDatas.initDatas()
    }
    LazyColumn {
        items(GalleryDatas.entries) {
            GalleryCard(it, Modifier.padding(16.dp))
        }
    }
}