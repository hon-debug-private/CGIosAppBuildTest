package io.github.honatsugiexpress.canvasegg.svgz.compressor.data.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.svgz.SvgzWriter
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
internal actual fun getWriterFromSvg(uri: String): SvgzWriter? {
    var writer: SvgzWriter? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        val content = window.fetch(uri, RequestInit())
            .then {
                it.text()
            }
            .await<JsString>()
            .toString()
        val document = Ksoup.parseXml(content)
        writer = SvgzWriter(document)
    }
    return writer
}