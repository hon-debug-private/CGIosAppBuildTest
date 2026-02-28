package io.github.honatsugiexpress.canvasegg.data.svg.type

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.TileMode
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.DrawableCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ElementCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.LinearGradientCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RadialGradientCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RootCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.boundingBox
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.cx
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.cy
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.fr
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.fx
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.fy
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.attrOrStyleOrNull
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.lenEnvWithResolver
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.r
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.root
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.x1
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.x2
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.y1
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.y2
import io.github.honatsugiexpress.canvasegg.data.svg.type.util.CssPaintToSvgPaintConverter
import io.github.honatsugiexpress.cssparser.data.css.paint.CssPaint
import io.github.honatsugiexpress.cssparser.data.css.paint.UnknownCssColor
import io.github.honatsugiexpress.cssparser.data.css.paint.generator.toCssString
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.min
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.RadialGradient as ComposeRadialGradient

@Serializable
sealed class SvgPaint {
    @Serializable
    data object None: SvgPaint() {
        override fun toSvgString() = "none"
    }
    @Serializable
    data class Color(
        val color: ComposeColor,
        val cssPaint: CssPaint
    ): SvgPaint() {
        override fun toSvgString(): String = cssPaint.toCssString()
        companion object {
            val Unknown = Color(ComposeColor.Unspecified, UnknownCssColor)
        }
    }
    sealed interface BrushPaint {
        fun toBrush(): Brush
    }
    @Serializable
    sealed class Gradient(
        @Transient open val stops: Map<SvgNormalizedFloat, Color> = emptyMap()
    ): SvgPaint(), BrushPaint
    @Serializable
    data class LinearGradient(
        override val stops: Map<SvgNormalizedFloat, Color>,
        val drawArea: SvgBoundingBox
    ): Gradient(stops) {
        override fun toSvgString(): String = "none"

        override fun toBrush(): Brush = Brush.linearGradient(
            *stops.map { (key, color) ->
                key.value to color.color
            }.toTypedArray(),
            start = Offset(drawArea.x, drawArea.y),
            end = Offset(drawArea.width, drawArea.height)
        )
    }
    @Serializable
    data class RadialGradient(
        override val stops: Map<SvgNormalizedFloat, Color>,
        val transformInfo: TransformInfo
    ): Gradient(stops) {
        override fun toSvgString(): String = "none"
        override fun toBrush(): Brush = Brush.radialGradient(
            *stops.map { (key, color) ->
                key.value to color.color
            }.toTypedArray(),
            center = Offset(
                x = (transformInfo.cx * transformInfo.targetRx) + transformInfo.targetX,
                y = (transformInfo.cy * transformInfo.targetRy) + transformInfo.targetY
            ),
            radius = transformInfo.r * min(transformInfo.targetRx, transformInfo.targetRy),
            tileMode = TileMode.Clamp
        ).also {
            it as ComposeRadialGradient
            it.transform = Matrix().apply {
                val (scaleX, scaleY) = calculateRatio(
                    transformInfo.targetRx * transformInfo.r,
                    transformInfo.targetRy * transformInfo.r
                )
                val translateX = (transformInfo.cx * transformInfo.targetRx) + transformInfo.targetX
                val translateY = (transformInfo.cy * transformInfo.targetRy) + transformInfo.targetY
                translate(translateX, translateY)
                scale(scaleX, scaleY)
                translate(-translateX, -translateY)
            }
        }
        private fun calculateRatio(x: Float, y: Float): Pair<Float, Float> {
            val minimumValue = min(x, y)

            if (minimumValue == 0f) {
                return Pair(x, y)
            }

            val ratioX = x / minimumValue
            val ratioY = y / minimumValue

            return Pair(ratioX, ratioY)
        }
        data class TransformInfo(
            val cx: Float,
            val cy: Float,
            val r: Float,
            val fx: Float,
            val fy: Float,
            val fr: Float,
            val targetX: Float,
            val targetY: Float,
            val targetRx: Float,
            val targetRy: Float
        )
    }
    @Serializable
    data class Url(val id: String): SvgPaint() {
        override fun toSvgString(): String = "url(#$id)"
    }
    companion object {
        private val converter by lazy {CssPaintToSvgPaintConverter() }
        fun urlResolve(root: RootCommand, target: DrawableCommand, url: Url): SvgPaint {
            var result: SvgPaint = None
            val density = root.env.density
            root
                .defs
                .asSequence()
                .filter {
                    it is ElementCommand && it.element.id() == url.id
                }
                .forEach { command ->
                    result = when (command) {
                        is LinearGradientCommand -> command
                            .stops
                            .associate { stopCommand ->
                                stopCommand.offset to stopCommand.color
                            }
                            .let {
                                val xLenEnv = command.lenEnvWithResolver { value ->
                                    value / 100f
                                }
                                val yLenEnv = command.lenEnvWithResolver { value ->
                                    value / 100f
                                }
                                val boundingBox = target.boundingBox()
                                val left = boundingBox.left
                                val top = boundingBox.top
                                val width = boundingBox.width
                                val height = boundingBox.height
                                val x1 = command.x1.toScreenValue(xLenEnv, density)
                                val y1 = command.y1.toScreenValue(yLenEnv, density)
                                val x2 = command.x2.toScreenValue(xLenEnv, density)
                                val y2 = command.y2.toScreenValue(yLenEnv, density)
                                LinearGradient(
                                    stops = it,
                                    drawArea = SvgBoundingBox.fromValues(
                                        left + (x1 * width),
                                        top + (y1 * height),
                                        left + (x2 * width),
                                        top + (y2 * height)
                                    )
                                )
                            }

                        is RadialGradientCommand -> command
                            .stops
                            .associate { stopCommand ->
                                stopCommand.offset to stopCommand.color
                            }
                            .let {
                                val xLenEnv = command.lenEnvWithResolver { value ->
                                    value / 100f
                                }
                                val yLenEnv = command.lenEnvWithResolver { value ->
                                    value / 100f
                                }
                                val rLenEnv = command.lenEnvWithResolver { value ->
                                    value / 100f
                                }
                                RadialGradient(
                                    stops = it,
                                    transformInfo = RadialGradient.TransformInfo(
                                        command.cx.toScreenValue(xLenEnv, density),
                                        command.cy.toScreenValue(yLenEnv, density),
                                        command.r.toScreenValue(rLenEnv, density),
                                        command.fx.toScreenValue(xLenEnv, density),
                                        command.fy.toScreenValue(yLenEnv, density),
                                        command.fr.toScreenValue(rLenEnv, density),
                                        target.boundingBox().left,
                                        target.boundingBox().top,
                                        target.boundingBox().width,
                                        target.boundingBox().height
                                    )
                                )
                            }
                        else -> None
                    }
                }
            return result
        }
        fun resolve(element: ElementCommand, attrName: String): SvgPaint {
            val value = element.attrOrStyleOrNull(attrName)
            return resolve(value, element.env)
        }
        fun resolve(value: String?, env: RenderEnv): SvgPaint {
            return when {
                value == null || value.equals("none", ignoreCase = true) -> None
                value.startsWith("url(") && value.endsWith(")") -> {
                    val url = value.removePrefix("url(").removeSuffix(")")
                    val id = url
                        .removeSurrounding("\"")
                        .removeSurrounding("\'")
                        .removePrefix("#")
                    Url(id)
                }

                else -> converter.parse(value, env)
            }
        }
    }

    abstract fun toSvgString(): String
}

inline val SvgPaint.Gradient.colors
    get() = stops.values.toList()

inline val SvgPaint.Gradient.offsets
    get() = stops.keys.toList()

internal fun SvgPaint.entity(command: DrawableCommand) = when (this) {
    is SvgPaint.Url -> SvgPaint.urlResolve((command as RenderCommand).root(), command, this)
    else -> this
}