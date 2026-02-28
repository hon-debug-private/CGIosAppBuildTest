package io.github.honatsugiexpress.cssparser.data.css.paint

import io.github.honatsugiexpress.cssparser.data.css.number.CssFloat
import kotlinx.serialization.Serializable

@Serializable
data class CssOklabColor(
    val lightness: CssFloat,
    val aAxis: CssFloat,
    val bAxis: CssFloat,
    val alpha: CssFloat
): CssBaseColor()