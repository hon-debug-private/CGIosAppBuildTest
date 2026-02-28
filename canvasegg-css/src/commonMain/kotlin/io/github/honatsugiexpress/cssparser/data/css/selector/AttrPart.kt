package io.github.honatsugiexpress.cssparser.data.css.selector

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.common.util.attrOrNull
import io.github.honatsugiexpress.cssparser.controller.CssSelectorProvider

data class AttrPart(
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

    override fun equalFeatures(provider: CssSelectorProvider): Boolean {
        val attrValue = provider.attr(name.name) ?: return false
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