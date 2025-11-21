package com.honatsugiexp.canvasegg.gallery.data

import androidx.compose.runtime.mutableStateListOf
import canvasegg.canvasegg_examples.canvasegg_gallery.generated.resources.FourRect
import canvasegg.canvasegg_examples.canvasegg_gallery.generated.resources.Res

object GalleryDatas {
    val entries: MutableList<GalleryData> by lazy {
        mutableStateListOf()
    }
    suspend fun initDatas() {
        entries.clear()
        entries += GalleryData(
            title = Res.string.FourRect,
            svgData = Res.readBytes("files/svg/four_rect.svg").decodeToString()
        )
    }
}