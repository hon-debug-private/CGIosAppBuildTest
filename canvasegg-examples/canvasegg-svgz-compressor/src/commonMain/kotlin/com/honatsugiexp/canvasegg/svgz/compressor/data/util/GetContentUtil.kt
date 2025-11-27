package com.honatsugiexp.canvasegg.svgz.compressor.data.util

import androidx.compose.runtime.Composable
import com.honatsugiexp.canvasegg.svgz.SvgzWriter

@Composable
internal expect fun getWriterFromSvg(uri: String): SvgzWriter?