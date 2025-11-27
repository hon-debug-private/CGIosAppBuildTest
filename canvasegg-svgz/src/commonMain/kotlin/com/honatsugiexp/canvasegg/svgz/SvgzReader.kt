package com.honatsugiexp.canvasegg.svgz

import com.fleeksoft.ksoup.nodes.Document
import okio.BufferedSource

expect class SvgzReader {
    fun read(source: BufferedSource): Document
}