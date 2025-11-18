package com.honatsugiexp.canvasegg.imageio

import com.fleeksoft.ksoup.nodes.Document
import com.honatsugiexp.canvasegg.data.svg.parser.SvgParserEnv
import okio.BufferedSink

class WebpImageIO(
    override val document: Document,
    override val env: SvgParserEnv
): ImageIoBase(document, env) {
    override fun writeTo(sink: BufferedSink) {
        image.writeTo(sink, Mimetype)
    }
    object Mimetype: ImageIoBase.Mimetype
}