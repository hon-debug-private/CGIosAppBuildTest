package io.github.honatsugiexpress.cssparser.data.css.selector

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.cssparser.antlr.CssParser
import io.github.honatsugiexpress.cssparser.controller.CssSelectorProvider
import io.github.honatsugiexpress.cssparser.data.css.selector.CombinatorPart.Type.Greater
import io.github.honatsugiexpress.cssparser.data.css.selector.CombinatorPart.Type.Plus
import io.github.honatsugiexpress.cssparser.data.css.selector.CombinatorPart.Type.Space
import io.github.honatsugiexpress.cssparser.data.css.selector.CombinatorPart.Type.Tiled

data class CombinatorPart(val type: Type): SelectorPart() {
    override fun equalFeatures(provider: CssSelectorProvider) = true
    companion object {
        operator fun invoke(ctx: CssParser.CombinatorContext) = when {
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