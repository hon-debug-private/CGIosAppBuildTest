package io.github.honatsugiexpress.cssparser.data.css.selector

import io.github.honatsugiexpress.cssparser.antlr.CssParser

data class NamespacedName(
    val nameSpace: Type,
    val name: String
) {
    sealed class Type {
        data object None: Type()
        data object All: Type()
        data class Name(val name: String): Type()
        companion object {
            fun fromPrefix(ctx: CssParser.TypeNamespacePrefixContext?): Type {
                return when {
                    ctx == null -> All
                    ctx.ident() != null -> Name(ctx.ident()!!.text)
                    ctx.Multiply() != null -> All
                    else -> None
                }
            }
        }
    }
}