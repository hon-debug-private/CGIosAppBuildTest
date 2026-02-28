package io.github.honatsugiexpress.cssparser.data.css.paint

import io.github.honatsugiexpress.cssparser.data.css.number.CssFloat
import kotlinx.serialization.Serializable

@Serializable
data class CssRgbColor(
    val red: CssFloat,
    val green: CssFloat,
    val blue: CssFloat,
    val alpha: CssFloat
): CssBaseColor()