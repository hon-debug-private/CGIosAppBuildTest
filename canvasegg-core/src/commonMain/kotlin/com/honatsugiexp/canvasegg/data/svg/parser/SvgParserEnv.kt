package com.honatsugiexp.canvasegg.data.svg.parser

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

data class SvgParserEnv(
    val density: Density,
    val layoutDirection: LayoutDirection,
    val option: SvgParserOption
) {
    companion object
}

@Composable
fun SvgParserEnv.Companion.get(option: SvgParserOption.() -> Unit = {}) = SvgParserEnv(
    density = LocalDensity.current,
    layoutDirection = LocalLayoutDirection.current,
    option = SvgParserOption().apply {
        option()
    }
)

fun SvgParserEnv.Companion.create(
    density: Density,
    layoutDirection: LayoutDirection,
    option: SvgParserOption.() -> Unit = {}
) = SvgParserEnv(
    density,
    layoutDirection,
    option = SvgParserOption().apply {
        option()
    }
)

class SvgParserOption {
    var drawArea: Path? = null
}