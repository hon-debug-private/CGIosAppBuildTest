package io.github.honatsugiexpress.cssparser.data.css.paint

import io.github.honatsugiexpress.cssparser.data.css.CssAngle
import io.github.honatsugiexpress.cssparser.data.css.number.CssFloat
import kotlinx.serialization.Serializable

@Serializable
data class CssOklchColor(
    val lightness: CssFloat,
    val chroma: CssFloat,
    val hue: CssAngle,
    val alpha: CssFloat
): CssBaseColor()