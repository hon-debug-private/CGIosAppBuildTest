package com.honatsugiexp.cssparser.antlr.selector

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.cssparser.antlr.Css3Parser
import com.honatsugiexp.cssparser.antlr.selector.CombinatorPart.Type.Greater
import com.honatsugiexp.cssparser.antlr.selector.CombinatorPart.Type.Plus
import com.honatsugiexp.cssparser.antlr.selector.CombinatorPart.Type.Space
import com.honatsugiexp.cssparser.antlr.selector.CombinatorPart.Type.Tiled

internal data class CombinatorPart(val type: Type): SelectorPart() {
    override fun equalFeatures(otherElement: Element) = true
    companion object {
        operator fun invoke(ctx: Css3Parser.CombinatorContext) = when {
            ctx.Plus() != null -> Plus
            ctx.Greater() != null -> Greater
            ctx.Tilde() != null -> Tiled
            ctx.Space() != null -> Space
            else -> error("Undefined separator")
        }
    }
    enum class Type {
        Plus,
        Greater,
        Tiled,
        Space
    }
}