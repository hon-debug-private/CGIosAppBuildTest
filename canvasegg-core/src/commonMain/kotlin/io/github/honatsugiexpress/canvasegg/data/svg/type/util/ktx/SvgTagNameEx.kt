package io.github.honatsugiexpress.canvasegg.data.svg.type.util.ktx

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName.Companion.CIRCLE
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName.Companion.ELLIPSE
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName.Companion.LINE
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName.Companion.PATH
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName.Companion.POLYGON
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName.Companion.POLYLINE
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName.Companion.RECT
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTagName.Companion.TEXT


@Suppress("NOTHING_TO_INLINE")
@PublishedApi
internal inline fun Element.svgTagName() =
    SvgTagName(tagName())

val SvgTagName.hasPath
    get() = when (this) {
        CIRCLE,
        ELLIPSE,
        LINE,
        PATH,
        POLYGON,
        POLYLINE,
        RECT,
        TEXT -> true

        else -> false
    }