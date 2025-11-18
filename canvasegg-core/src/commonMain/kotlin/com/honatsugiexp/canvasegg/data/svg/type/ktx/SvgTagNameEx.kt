package com.honatsugiexp.canvasegg.data.svg.type.ktx

import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName.Companion.CIRCLE
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName.Companion.ELLIPSE
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName.Companion.LINE
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName.Companion.PATH
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName.Companion.POLYGON
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName.Companion.POLYLINE
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName.Companion.RECT
import com.honatsugiexp.canvasegg.data.svg.type.SvgTagName.Companion.TEXT


internal fun Element.svgTagName() = SvgTagName(tagName())

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