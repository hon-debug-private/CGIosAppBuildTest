package io.github.honatsugiexpress.cssparser.data.css.paint

import io.github.honatsugiexpress.cssparser.data.css.CssAngle
import io.github.honatsugiexpress.cssparser.data.css.number.CssFloat
import kotlinx.serialization.Serializable

@Serializable
data class CssHwbColor(
    val hue: CssAngle,
    val whiteness: CssFloat,
    val blackness: CssFloat,
    val alpha: CssFloat
): CssBaseColor()