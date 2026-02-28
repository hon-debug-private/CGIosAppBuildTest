package io.github.honatsugiexpress.canvasegg.data.svg.type.font

enum class FontDisplay {
    Block,
    Swap,
    Fallback,
    Optional;
    companion object {
        fun fromString(value: String) = when (value) {
            "auto", "block" -> Block
            "swap" -> Swap
            "fallback" -> Fallback
            "optional" -> Optional
            else -> Block
        }
    }
}