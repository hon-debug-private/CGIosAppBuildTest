package io.github.honatsugiexpress.canvasegg.svgz.compressor.data.util

import androidx.compose.runtime.Composable
import io.github.honatsugiexpress.canvasegg.svgz.SvgzWriter

@Composable
internal expect fun getWriterFromSvg(uri: String): SvgzWriter?