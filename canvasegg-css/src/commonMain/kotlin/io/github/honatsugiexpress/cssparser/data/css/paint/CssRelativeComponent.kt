package io.github.honatsugiexpress.cssparser.data.css.paint

import kotlinx.serialization.Serializable

@Serializable
sealed class CssRelativeComponent {
    @Serializable
    data class Original(val id: String): CssRelativeComponent()
    @Serializable
    data class NewValue(val value: String): CssRelativeComponent()
}