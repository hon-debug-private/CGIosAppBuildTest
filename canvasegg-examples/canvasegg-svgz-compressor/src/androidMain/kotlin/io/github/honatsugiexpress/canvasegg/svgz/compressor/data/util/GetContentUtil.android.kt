package io.github.honatsugiexpress.canvasegg.svgz.compressor.data.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.github.honatsugiexpress.canvasegg.svgz.SvgzWriter
import androidx.core.net.toUri
import com.fleeksoft.ksoup.Ksoup

@Composable
internal actual fun getWriterFromSvg(uri: String): SvgzWriter? {
    val contentResolver = LocalContext.current.contentResolver
    return contentResolver.openInputStream(uri.toUri())?.use {
        val content = it.bufferedReader().readText()
        val document = Ksoup.parseXml(content)
        SvgzWriter(document)
    }
}