package com.honatsugiexp.cssparser.antlr.selector

import com.honatsugiexp.cssparser.antlr.Css3Parser

internal data class NamespacedName(
    val nameSpace: Type,
    val name: String
) {
    sealed class Type {
        data object None: Type()
        data object All: Type()
        data class Name(val name: String): Type()
        companion object {
            fun fromPrefix(ctx: Css3Parser.TypeNamespacePrefixContext?): Type {
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