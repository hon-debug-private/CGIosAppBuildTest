package io.github.honatsugiexpress.canvasegg.svgz.compressor.data.util

import androidx.compose.runtime.Composable
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.svgz.SvgzWriter
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun getWriterFromSvg(uri: String): SvgzWriter? {
    val content: String? = try {
        NSString.stringWithContentsOfFile(
            path = uri,
            encoding = NSUTF8StringEncoding,
            error = null
        )
    } catch (_: Exception) {
        return null
    }

    return content?.let {
        try {
            val document = Ksoup.parseXml(content)
            SvgzWriter(document)
        } catch (e: Exception) {
            null
        }
    }
}