package io.github.honatsugiexpress.cssparser.data.css.paint

import kotlinx.serialization.Serializable

@Serializable
data class CssNamedColor(
    val hexColor: CssHexColor,
    val name: String
): CssBaseColor()