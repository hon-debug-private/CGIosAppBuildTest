package io.github.honatsugiexpress.cssparser.data.css.paint

import kotlinx.serialization.Serializable

@Serializable
data class CssFromColor(
    val original: CssPaint,
    val components: List<CssRelativeComponent>,
    val alpha: Float
): CssBaseColor()