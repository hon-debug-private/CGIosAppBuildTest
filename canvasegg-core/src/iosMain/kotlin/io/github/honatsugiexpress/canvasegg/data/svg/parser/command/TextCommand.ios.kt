package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.Path

internal actual fun TextCommand.nativePath(): Path {
    return Path()
}