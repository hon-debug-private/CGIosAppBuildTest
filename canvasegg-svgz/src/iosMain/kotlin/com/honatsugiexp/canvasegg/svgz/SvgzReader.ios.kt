package com.honatsugiexp.canvasegg.svgz

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import okio.BufferedSource
import okio.GzipSource
import okio.buffer
import okio.use

actual class SvgzReader {
    actual fun read(source: BufferedSource): Document {
        return GzipSource(source).buffer().use {
            val data = it.readByteArray()
            Ksoup.parseXml(data.decodeToString())
        }
    }
}