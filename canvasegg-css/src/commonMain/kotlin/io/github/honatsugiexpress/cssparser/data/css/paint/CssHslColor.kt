package io.github.honatsugiexpress.cssparser.data.css.paint

import io.github.honatsugiexpress.cssparser.data.css.CssAngle
import io.github.honatsugiexpress.cssparser.data.css.number.CssFloat
import kotlinx.serialization.Serializable

@Serializable
data class CssHslColor(
    val hue: CssAngle,
    val saturation: CssFloat,
    val lightness: CssFloat,
    val alpha: CssFloat
): CssBaseColor()