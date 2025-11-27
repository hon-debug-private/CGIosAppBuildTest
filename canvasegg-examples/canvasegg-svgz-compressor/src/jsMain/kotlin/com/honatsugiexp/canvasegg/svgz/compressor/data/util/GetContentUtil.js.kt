package com.honatsugiexp.canvasegg.svgz.compressor.data.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.fleeksoft.ksoup.Ksoup
import com.honatsugiexp.canvasegg.svgz.SvgzWriter
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit

@Composable
internal actual fun getWriterFromSvg(uri: String): SvgzWriter? {
    var writer: SvgzWriter? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        val document = window.fetch(uri, RequestInit())
            .then {
                it.text()
            }
            .then {
                Ksoup.parseXml(it)
            }
            .await()
        writer = SvgzWriter(document)
    }
    return writer
}