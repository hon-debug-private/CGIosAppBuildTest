package io.github.honatsugiexpress.cssparser.antlr

import io.github.honatsugiexpress.cssparser.antlr.CssLexer.Tokens
import io.github.honatsugiexpress.cssparser.data.css.fontface.FontFaceDeclaration
import io.github.honatsugiexpress.cssparser.data.css.selector.AttrPart
import io.github.honatsugiexpress.cssparser.data.css.selector.ClassPart
import io.github.honatsugiexpress.cssparser.data.css.selector.CombinatorPart
import io.github.honatsugiexpress.cssparser.data.css.selector.TagPart
import io.github.honatsugiexpress.cssparser.data.css.selector.IdPart
import io.github.honatsugiexpress.cssparser.data.css.selector.NamespacedName
import io.github.honatsugiexpress.cssparser.data.css.selector.NegationPart
import io.github.honatsugiexpress.cssparser.data.css.selector.SelectorPart
import io.github.honatsugiexpress.cssparser.data.css.selector.UnknownPart
import io.github.honatsugiexpress.cssparser.controller.CssStyleController
import io.github.honatsugiexpress.cssparser.data.css.CssRuleset
import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.antlr.v4.kotlinruntime.tree.TerminalNode

typealias NestedSelectorParts = List<SelectorParts>
typealias SelectorParts = List<SelectorPart>

class CssStyleVisitor(val controller: CssStyleController): CssParserBaseVisitor<Any>() {
    override fun defaultResult(): Any = Unit
    override fun visitKnownRuleset(ctx: CssParser.KnownRulesetContext) {
        val selectorParts = visitSelectorGroup(ctx.selectorGroup())
        val declarationList = ctx.declarationList()?.let {
            visitDeclarationList(it)
        } ?: emptyMap()
        val ruleset = CssRuleset(selectorParts, declarationList)
        controller.addRuleset(ruleset)
    }

    override fun visitSelectorGroup(ctx: CssParser.SelectorGroupContext): NestedSelectorParts {
        return ctx.selector().map {
            visitSelector(it)
        }
    }

    override fun visitSelector(ctx: CssParser.SelectorContext): SelectorParts {
        return ctx.simpleSelectorSequence().flatMapIndexed { index, selectorSequenceCtx ->
            buildList {
                addAll(visitSimpleSelectorSequence(selectorSequenceCtx))
                ctx.combinator(index)?.takeUnless { index == 0 }?.let {
                    CombinatorPart(
                        it
                    )
                }
            }
        }
    }

    override fun visitSimpleSelectorSequence(ctx: CssParser.SimpleSelectorSequenceContext): SelectorParts {
        return ctx.children?.map { node ->
            getSelectorPart(node)
        } ?: emptyList()
    }

    override fun visitDeclarationList(ctx: CssParser.DeclarationListContext): Map<String, String> {
        return ctx.declaration().associate {
            when (it) {
                is CssParser.KnownDeclarationContext -> visitKnownDeclaration(it)
                is CssParser.UnknownDeclarationContext -> visitUnknownDeclaration(it)
                else -> error("Unexpected declaration definition found")
            }
        }
    }

    override fun visitKnownDeclaration(ctx: CssParser.KnownDeclarationContext): Pair<String, String> {
        val key = ctx.property_().text
        val value = ctx.expr().text
        return key to value
    }

    override fun visitUnknownDeclaration(ctx: CssParser.UnknownDeclarationContext): Pair<String, String> {
        val key = ctx.property_().text
        val value = ctx.value().text
        return key to value
    }
    private fun getSelectorPart(tree: ParseTree): SelectorPart {
        return when (tree) {
            is TerminalNode -> when (tree.symbol.type) {
                Tokens.Hash -> IdPart(
                    tree.text.removePrefix("#")
                )
                else -> UnknownPart
            }

            is CssParser.ClassNameContext -> ClassPart(
                tree.ident().text
            )
            is CssParser.AttribContext -> {
                val attrId =
                    NamespacedName(
                        NamespacedName.Type.fromPrefix(
                            tree.typeNamespacePrefix()
                        ),
                        tree.ident(0)!!.text
                    )
                val matchType = when {
                    tree.PrefixMatch() != null -> AttrPart.Type.PrefixMatch
                    tree.SuffixMatch() != null -> AttrPart.Type.SuffixMatch
                    tree.SubstringMatch() != null -> AttrPart.Type.SubstringMatch
                    tree.Equal() != null -> AttrPart.Type.Equal
                    tree.Includes() != null -> AttrPart.Type.Includes
                    tree.DashMatch() != null -> AttrPart.Type.DashMatch
                    else -> AttrPart.Type.Unknown
                }
                val value = tree.String_()?.text?.removeSurrounding("\"") ?: tree.ident(2)!!.text
                AttrPart(
                    attrId,
                    matchType,
                    value
                )
            }

            is CssParser.TypeSelectorContext -> TagPart(
                NamespacedName(
                    NamespacedName.Type.fromPrefix(
                        tree.typeNamespacePrefix()
                    ),
                    tree.elementName().text
                )
            )

            is CssParser.NegationContext -> tree.negationArg().children?.let {
                NegationPart(
                    it.map {
                        getSelectorPart(it)
                    }
                )
            } ?: NegationPart.Empty

            else -> UnknownPart
        }
    }

    override fun visitFontFaceRule(ctx: CssParser.FontFaceRuleContext) {
        val rawDeclaration = ctx.fontFaceDeclaration().associate {
            when (it) {
                is CssParser.KnownFontFaceDeclarationContext -> visitKnownFontFaceDeclaration(it)
                is CssParser.UnknownFontFaceDeclarationContext -> visitUnknownFontFaceDeclaration(it)
                else -> Pair("", emptyList())
            }
        }
        val declaration = FontFaceDeclaration.fromRawDeclaration(rawDeclaration)
        controller.addFontFace(declaration)
    }

    override fun visitKnownFontFaceDeclaration(ctx: CssParser.KnownFontFaceDeclarationContext): Pair<String, List<String>> {
        return ctx.property_().text to ctx.expr().term().map {
            it.text
        }
    }

    override fun visitUnknownFontFaceDeclaration(ctx: CssParser.UnknownFontFaceDeclarationContext): Pair<String, List<String>> {
        return ctx.property_().text to listOf(ctx.value().text)
    }
}