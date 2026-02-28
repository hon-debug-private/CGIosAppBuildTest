package io.github.honatsugiexpress.cssparser.data.css.fontface

import io.github.honatsugiexpress.canvasegg.common.log.CanvasEggLogger

class FontFaceDeclaration {
    var ascentOverride: String = "normal"
    var descentOverride: String = "normal"
    var fontFamily: String = ""
    var fontDisplay: String = "auto"
    var fontStyle: String = "normal"
    var fontWeight: String = ""
    var fontFeatureSettings: List<String> = emptyList()
    var fontVariationSettings: List<String> = emptyList()
    var sizeAdjust: String = ""
    var src: List<String> = emptyList()
    var invalid = false
    companion object {
        fun fromRawDeclaration(rawValue: Map<String, List<String>>): FontFaceDeclaration {
            return FontFaceDeclaration()
                .apply {
                    ascentOverride = rawValue.getOrElse("ascent-override") { listOf("normal") }.first()
                    descentOverride = rawValue.getOrElse("descent-override") { listOf("normal") }.first()
                    fontDisplay = rawValue.getOrElse("font-display") { listOf("auto") }.first()
                    fontFamily = rawValue.getOrElse("font-family") {
                        invalid = true
                        emptyList()
                    }.joinToString(" ")
                    fontStyle = rawValue.getOrElse("font-display") { listOf("normal") }.first()
                    fontWeight = rawValue.getOrElse("font-weight") { listOf("normal") }.first()
                    fontFeatureSettings = rawValue.getOrElse("font-feature-settings") { listOf("normal") }
                    fontVariationSettings = rawValue.getOrElse("font-variation-settings") { listOf("normal") }
                    sizeAdjust = rawValue.getOrElse("size-adjust") { listOf("100%") }.first()
                    src = rawValue.getOrElse("src") {
                        invalid = true
                        emptyList()
                    }
                    if (rawValue.containsKey("line-gap-override")) {
                        CanvasEggLogger
                            .tag("FontFaceDeclaration")
                            .warn("The line-gap-override property is not supported in CanvasEgg due to environmental constraints.")
                    }
                }
        }
    }
}