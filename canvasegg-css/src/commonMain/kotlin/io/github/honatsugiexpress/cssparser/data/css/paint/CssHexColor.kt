package io.github.honatsugiexpress.cssparser.data.css.paint

import kotlinx.serialization.Serializable

@Serializable
data class CssHexColor(val hexValue: String): CssBaseColor()