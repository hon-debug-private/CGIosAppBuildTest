package io.github.honatsugiexpress.canvasegg.data.svg.type.font

import androidx.compose.ui.text.font.FontWeight
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgNormalizedFloat
import io.github.honatsugiexpress.cssparser.data.css.fontface.FontFaceDeclaration

data class SvgFontFace(
    val ascentOverride: SvgNormalizedFloat,
    val descentOverride: SvgNormalizedFloat,
    val fontFamily: String,
    val fontDisplay: FontDisplay,
    val fontStyle: SvgFontStyle,
    val fontWeight: FontWeight,
    val fontFeatureSettings: List<String>,
    val fontVariationSettings: List<String>,
    val sizeAdjust: SvgNormalizedFloat,
    val src: SvgFontFaceSrc
) {
    companion object {
        fun fromDeclaration(declaration: FontFaceDeclaration): SvgFontFace {
            val fontFeatureSettings = declaration.fontFeatureSettings
            val fontVariationSettings = declaration.fontVariationSettings
            return SvgFontFace(
                SvgNormalizedFloat(
                    declaration.ascentOverride
                ),
                SvgNormalizedFloat(
                    declaration.descentOverride
                ),
                declaration.fontFamily,
                FontDisplay.fromString(declaration.fontDisplay),
                SvgFontStyle(
                    declaration.fontStyle
                ),
                absoluteStringToFontWeight(
                    declaration.fontWeight
                ),
                if (fontFeatureSettings.size == 1 && fontFeatureSettings[0] == "normal") {
                    emptyList()
                } else {
                    declaration.fontFeatureSettings
                },
                if (fontVariationSettings.size == 1 && fontVariationSettings[0] == "normal") {
                    emptyList()
                } else {
                    declaration.fontVariationSettings
                },
                SvgNormalizedFloat(
                    declaration.sizeAdjust
                ),
                SvgFontFaceSrc.fromString(declaration.src)
            )
        }
    }
}