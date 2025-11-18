package com.honatsugiexp.canvasegg.imageio

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import com.fleeksoft.ksoup.nodes.Document
import com.honatsugiexp.canvasegg.data.svg.parser.SvgCanvasParser
import com.honatsugiexp.canvasegg.data.svg.parser.SvgParserEnv
import com.honatsugiexp.canvasegg.data.svg.type.SvgLength
import com.honatsugiexp.canvasegg.data.svg.type.toScreenValue
import com.honatsugiexp.cssparser.ElementStyleController
import okio.BufferedSink
import kotlin.math.roundToInt

sealed class ImageIoBase(
    open val document: Document,
    open val env: SvgParserEnv
) {
    protected val image: ImageBitmap by lazy {
        SvgCanvasParser(document, env).run {
            val root = document.firstElementChild()!!
            val controller = ElementStyleController(root)
            controller.parseDocumentStyles()
            val density = env.density.density
            val lenEnv = SvgLength.Env.fromCommand(root, density)
            val width = SvgLength(controller.attrOrStyleOrNull("width"))
            val height = SvgLength(controller.attrOrStyleOrNull("height"))
            val localImage = ImageBitmap(
                width = width.toScreenValue(lenEnv, density).roundToInt(),
                height = height.toScreenValue(lenEnv, density).roundToInt()
            )
            val canvas = Canvas(localImage)
            CanvasDrawScope().apply {
                draw(
                    env.density,
                    env.layoutDirection,
                    canvas,
                    Size(
                        width.toScreenValue(lenEnv, density),
                        height.toScreenValue(lenEnv, density)
                    )
                ) {
                    draw(this)
                }
            }
            localImage
        }
    }
    abstract fun writeTo(sink: BufferedSink)
    sealed interface Mimetype
}