package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.graphics.Path
import androidx.core.graphics.TypefaceCompat

internal actual fun TextCommand.nativePath(): Path {
    return Path()
}