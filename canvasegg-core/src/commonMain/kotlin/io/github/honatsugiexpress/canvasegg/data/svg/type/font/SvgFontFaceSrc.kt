package io.github.honatsugiexpress.canvasegg.data.svg.type.font

class SvgFontFaceSrc(
    private val keywords: List<SvgFontFaceSrcKeyword>
): List<SvgFontFaceSrcKeyword> by keywords {
    companion object {
        fun fromString(content: List<String>): SvgFontFaceSrc {
            val keywords = content.map { part ->
                when {
                    part.startsWith("url(") && part.endsWith(")") -> SvgFontFaceSrcKeyword.Url(
                        part.removeSurrounding("url(\"", "\")")
                    )
                    else -> SvgFontFaceSrcKeyword.Unknown
                }
            }
            return SvgFontFaceSrc(keywords)
        }
    }
}

sealed class SvgFontFaceSrcKeyword {
    data class Url(val value: String): SvgFontFaceSrcKeyword()
    data object Unknown: SvgFontFaceSrcKeyword()
}