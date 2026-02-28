package io.github.honatsugiexpress.canvasegg.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.fleeksoft.ksoup.nodes.Document
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser

@Suppress("unused")
@Composable
fun SvgCanvas(
    parser: SvgCanvasParser,
    modifier: Modifier = Modifier
) {
    Canvas(modifier) {
        parser.draw(this)
    }
}