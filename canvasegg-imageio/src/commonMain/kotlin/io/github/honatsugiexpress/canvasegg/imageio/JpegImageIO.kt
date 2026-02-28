package io.github.honatsugiexpress.canvasegg.imageio

import com.fleeksoft.ksoup.nodes.Document
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import okio.BufferedSink

class JpegImageIO(
    override val document: Document,
    override val env: SvgParserEnv
): ImageIoBase(document, env) {
    override fun writeTo(sink: BufferedSink) {
        image.writeTo(sink, Mimetype)
    }
    object Mimetype: ImageIoBase.Mimetype
}