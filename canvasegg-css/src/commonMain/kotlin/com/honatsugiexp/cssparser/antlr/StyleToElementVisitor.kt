package com.honatsugiexp.cssparser.antlr

import com.honatsugiexp.cssparser.antlr.Css3Lexer.Tokens
import com.honatsugiexp.cssparser.ElementStyleController
import com.honatsugiexp.cssparser.antlr.selector.AttrPart
import com.honatsugiexp.cssparser.antlr.selector.ClassPart
import com.honatsugiexp.cssparser.antlr.selector.CombinatorPart
import com.honatsugiexp.cssparser.antlr.selector.ElementPart
import com.honatsugiexp.cssparser.antlr.selector.IdPart
import com.honatsugiexp.cssparser.antlr.selector.NamespacedName
import com.honatsugiexp.cssparser.antlr.selector.NegationPart
import com.honatsugiexp.cssparser.antlr.selector.SelectorPart
import com.honatsugiexp.cssparser.antlr.selector.UnknownPart
import org.antlr.v4.kotlinruntime.tree.ParseTree
import org.antlr.v4.kotlinruntime.tree.TerminalNode

internal typealias NestedSelectorParts = List<SelectorParts>
internal typealias SelectorParts = List<SelectorPart>

internal class StyleToElementVisitor(private val controller: ElementStyleController): Css3ParserBaseVisitor<Any>() {
    override fun visitKnownRuleset(ctx: Css3Parser.KnownRulesetContext) {
        val selectorParts = visitSelectorGroup(ctx.selectorGroup())
        val declarationList = ctx.declarationList()?.let {
            visitDeclarationList(it)
        } ?: emptyMap()
        println(selectorParts)
        if (controller.equalSelector(selectorParts)) {
            controller.addStyles(declarationList)
        }
    }

    override fun visitSelectorGroup(ctx: Css3Parser.SelectorGroupContext): NestedSelectorParts {
        return ctx.selector().map {
            visitSelector(it)
        }
    }

    override fun visitSelector(ctx: Css3Parser.SelectorContext): SelectorParts {
        return ctx.simpleSelectorSequence().flatMapIndexed { index, selectorSequenceCtx ->
            buildList {
                addAll(visitSimpleSelectorSequence(selectorSequenceCtx))
                ctx.combinator(index)?.takeUnless { index == 0 }?.let {
                    CombinatorPart(it)
                }
            }
        }
    }

    override fun visitSimpleSelectorSequence(ctx: Css3Parser.SimpleSelectorSequenceContext): SelectorParts {
        return ctx.children?.map { node ->
            getSelectorPart(node)
        } ?: emptyList()
    }

    override fun visitDeclarationList(ctx: Css3Parser.DeclarationListContext): Map<String, String> {
        return ctx.declaration().associate {
            when (it) {
                is Css3Parser.KnownDeclarationContext -> visitKnownDeclaration(it)
                is Css3Parser.UnknownDeclarationContext -> visitUnknownDeclaration(it)
                else -> error("Unexpected declaration definition found")
            }
        }
    }

    override fun visitKnownDeclaration(ctx: Css3Parser.KnownDeclarationContext): Pair<String, String> {
        val key = ctx.property_().text
        val value = ctx.expr().text
        return key to value
    }

    override fun visitUnknownDeclaration(ctx: Css3Parser.UnknownDeclarationContext): Pair<String, String> {
        val key = ctx.property_().text
        val value = ctx.value().text
        return key to value
    }
    private fun getSelectorPart(tree: ParseTree): SelectorPart {
        return when (tree) {
            is TerminalNode -> when (tree.symbol.type) {
                Tokens.Hash -> IdPart(tree.text.removePrefix("#"))
                else -> UnknownPart
            }

            is Css3Parser.ClassNameContext -> ClassPart(tree.ident().text)
            is Css3Parser.AttribContext -> {
                val attrId = NamespacedName(
                    NamespacedName.Type.fromPrefix(tree.typeNamespacePrefix()),
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
                AttrPart(attrId, matchType, value)
            }

            is Css3Parser.TypeSelectorContext -> ElementPart(
                NamespacedName(
                    NamespacedName.Type.fromPrefix(tree.typeNamespacePrefix()),
                    tree.elementName().text
                )
            )

            is Css3Parser.NegationContext -> tree.negationArg().children?.let {
                NegationPart(
                    it.map {
                        getSelectorPart(it)
                    }
                )
            } ?: NegationPart.Empty

            else -> UnknownPart
        }
    }
    override fun defaultResult() = Unit
}