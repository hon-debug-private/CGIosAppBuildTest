package com.honatsugiexp.canvasegg.data.svg.parser

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

data class SvgParserEnv(
    val density: Density,
    val layoutDirection: LayoutDirection
) {
    companion object
}

@Composable
fun SvgParserEnv.Companion.get() = SvgParserEnv(
    density = LocalDensity.current,
    layoutDirection = LocalLayoutDirection.current
)