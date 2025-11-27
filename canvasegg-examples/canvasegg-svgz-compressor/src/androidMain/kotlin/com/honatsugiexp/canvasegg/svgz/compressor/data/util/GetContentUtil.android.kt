package com.honatsugiexp.canvasegg.svgz.compressor.data.util

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.fleeksoft.ksoup.nodes.Document
import com.honatsugiexp.canvasegg.svgz.SvgzWriter
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