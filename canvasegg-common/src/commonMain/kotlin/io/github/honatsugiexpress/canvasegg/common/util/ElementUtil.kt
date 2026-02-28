package io.github.honatsugiexpress.canvasegg.common.util

import com.fleeksoft.ksoup.nodes.Element

@Suppress("NOTHING_TO_INLINE")
inline fun Element.attrOrNull(attrName: String) = if (hasAttr(attrName)) {
    attr(attrName)
} else null