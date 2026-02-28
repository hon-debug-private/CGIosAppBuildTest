package io.github.honatsugiexpress.cssparser.data.css.paint

import kotlinx.serialization.Serializable

@Serializable
data class CssLightDark(
    val light: CssPaint,
    val dark: CssPaint
): CssPaint()