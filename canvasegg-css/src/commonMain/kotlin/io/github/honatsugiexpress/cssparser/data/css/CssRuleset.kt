package io.github.honatsugiexpress.cssparser.data.css

import io.github.honatsugiexpress.cssparser.antlr.NestedSelectorParts

data class CssRuleset(
    val selectors: NestedSelectorParts,
    val values: Map<String, String>
)