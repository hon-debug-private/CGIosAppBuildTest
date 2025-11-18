package com.honatsugiexp.canvasegg.common.util

import com.fleeksoft.ksoup.nodes.Element

fun Element.attrOrNull(attrName: String) = if (hasAttr(attrName)) {
    attr(attrName)
} else null