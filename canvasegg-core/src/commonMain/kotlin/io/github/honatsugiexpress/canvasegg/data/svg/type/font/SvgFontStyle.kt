package io.github.honatsugiexpress.canvasegg.data.svg.type.font

import androidx.compose.ui.text.font.FontStyle
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgAngle

sealed class SvgFontStyle {
    data object Normal: SvgFontStyle()
    data object Italic: SvgFontStyle()
    data class Oblique(
        val first: SvgAngle?,
        val second: SvgAngle?
    ): SvgFontStyle()
}

fun SvgFontStyle(content: String): SvgFontStyle = when (content) {
    "normal" -> SvgFontStyle.Normal
    "italic" -> SvgFontStyle.Italic
    "oblique" -> SvgFontStyle.Oblique(null, null)
    else -> {
        val parts = content.split("\\s".toRegex())
        when (parts.size) {
            3 if parts[0] == "oblique" -> {
                SvgFontStyle.Oblique(
                    SvgAngle(
                        parts[1]
                    ),
                    SvgAngle(
                        parts[2]
                    )
                )
            }

            2 if parts[0] == "oblique" -> {
                SvgFontStyle.Oblique(
                    SvgAngle(
                        parts[1]
                    ), null)
            }

            else -> SvgFontStyle.Normal
        }
    }
}

fun SvgFontStyle.toComposeStyle() = when (this) {
    is SvgFontStyle.Normal -> FontStyle.Normal
    is SvgFontStyle.Italic -> FontStyle.Italic
    is SvgFontStyle.Oblique -> FontStyle.Normal
}