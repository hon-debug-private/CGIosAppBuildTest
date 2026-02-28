package io.github.honatsugiexpress.canvasegg.imageio

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import com.fleeksoft.ksoup.nodes.Document
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.toPxValue
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleController
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
            val xLenEnv = SvgLength.Env.fromElement(root, density, SvgLength.Env.RefDirection.X)
            val yLenEnv = SvgLength.Env.fromElement(root, density, SvgLength.Env.RefDirection.Y)
            val width = SvgLength(controller.attrOrStyleOrNull("width")).toPxValue(xLenEnv)
            val height = SvgLength(controller.attrOrStyleOrNull("height")).toPxValue(yLenEnv)
            val localImage = ImageBitmap(
                width = width.roundToInt(),
                height = height.roundToInt()
            )
            val canvas = Canvas(localImage)
            CanvasDrawScope().apply {
                draw(
                    env.density,
                    env.layoutDirection,
                    canvas,
                    Size(width, height)
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