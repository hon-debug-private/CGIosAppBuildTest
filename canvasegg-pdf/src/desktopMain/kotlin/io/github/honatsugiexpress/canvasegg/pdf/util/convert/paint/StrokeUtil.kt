package io.github.honatsugiexpress.canvasegg.pdf.util.convert.paint

import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin

internal fun StrokeCap.toPDLineCap() = when (this) {
    StrokeCap.Butt -> 0
    StrokeCap.Round -> 1
    StrokeCap.Square -> 2
    else -> 0
}

internal fun StrokeJoin.toPDLineJoin() = when (this) {
    StrokeJoin.Miter -> 0
    StrokeJoin.Round -> 1
    StrokeJoin.Bevel -> 2
    else -> 0
}