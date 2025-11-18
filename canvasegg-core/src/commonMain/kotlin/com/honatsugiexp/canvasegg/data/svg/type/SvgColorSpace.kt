package com.honatsugiexp.canvasegg.data.svg.type

import androidx.compose.runtime.Composable
import canvasegg.canvasegg_core.generated.resources.PredefinedColor
import canvasegg.canvasegg_core.generated.resources.Res
import canvasegg.canvasegg_core.generated.resources.Unknown
import org.jetbrains.compose.resources.stringResource

enum class SvgColorSpace {
    Unspecified,
    Keyword,
    Hex,
    Rgb,
    Hsl,
    Hwb,
    Lab,
    Lch,
    Oklab,
    Oklch,
    DisplayP3,
    Rec2020;
    @Composable
    fun toComposeText(): String {
        return when (this) {
            Keyword -> stringResource(Res.string.PredefinedColor)
            Hex -> "HEX"
            Rgb -> "RGB"
            Hsl -> "HSL"
            Hwb -> "HWB"
            Lab -> "Lab"
            Lch -> "LCH"
            Oklab -> "Oklab"
            Oklch -> "Oklch"
            DisplayP3 -> "DisplayP3"
            Rec2020 -> "Rec2020"
            else -> stringResource(Res.string.Unknown)
        }
    }
}