package com.honatsugiexp.canvasegg.svgz

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import okio.BufferedSource

//actual class SvgzReader {
//    actual fun read(source: BufferedSource): Document {
//        return Ksoup.parseXml(
//            decompressGzip(
//                source.readByteArray()
//            ).decodeToString()
//        )
//    }
//}