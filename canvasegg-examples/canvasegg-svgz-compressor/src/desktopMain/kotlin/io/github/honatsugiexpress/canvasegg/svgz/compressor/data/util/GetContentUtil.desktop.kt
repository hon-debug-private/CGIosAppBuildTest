package io.github.honatsugiexpress.canvasegg.svgz.compressor.data.util

import androidx.compose.runtime.Composable
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.svgz.SvgzWriter
import java.io.File

@Composable
internal actual fun getWriterFromSvg(uri: String): SvgzWriter? {
    val file = File(uri)
    val content = file.readText()
    val document = Ksoup.parseXml(content)
    return SvgzWriter(document)
}