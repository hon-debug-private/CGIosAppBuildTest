package com.honatsugiexp.cssparser.antlr.selector

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.common.util.attrOrNull

internal data class AttrPart(
    val name: NamespacedName,
    val type: Type,
    val value: String
): SelectorPart() {
    enum class Type {
        PrefixMatch,
        SuffixMatch,
        SubstringMatch,
        Equal,
        Includes,
        DashMatch,
        Unknown
    }

    override fun equalFeatures(otherElement: Element): Boolean {
        val attrValue = otherElement.attrOrNull(name.name) ?: return false
        println("$attrValue $value ${attrValue == value}")
        return when (type) {
            Type.PrefixMatch -> attrValue.startsWith(value)
            Type.SuffixMatch -> attrValue.endsWith(value)
            Type.SubstringMatch -> attrValue.contains(value)
            Type.Equal -> attrValue == value
            Type.Includes -> attrValue.trim().split("\\s+".toRegex()).contains(value)
            Type.DashMatch -> attrValue == value || attrValue.startsWith("$value-")
            Type.Unknown -> false
        }
    }
}