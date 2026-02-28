package io.github.honatsugiexpress.canvasegg.gallery.data

internal expect object PlatformCheck {
    val current: Platform
}

internal enum class Platform {
    Android,
    Ios,
    Desktop,
    Wasm,
    Js
}