package com.honatsugiexp.canvasegg.data.svg.type

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import com.honatsugiexp.canvasegg.data.svg.parser.command.DefCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.HasParentCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.LinearGradientCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RadialGradientCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RootCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.childrenList
import com.honatsugiexp.canvasegg.data.svg.parser.command.cx
import com.honatsugiexp.canvasegg.data.svg.parser.command.cy
import com.honatsugiexp.canvasegg.data.svg.parser.command.fr
import com.honatsugiexp.canvasegg.data.svg.parser.command.fx
import com.honatsugiexp.canvasegg.data.svg.parser.command.fy
import com.honatsugiexp.canvasegg.data.svg.parser.command.ktx.element.attrOrStyleOrNull
import com.honatsugiexp.canvasegg.data.svg.parser.command.ktx.element.lenEnv
import com.honatsugiexp.canvasegg.data.svg.parser.command.r
import com.honatsugiexp.canvasegg.data.svg.parser.command.root
import com.honatsugiexp.canvasegg.data.svg.parser.command.x1
import com.honatsugiexp.canvasegg.data.svg.parser.command.x2
import com.honatsugiexp.canvasegg.data.svg.parser.command.y1
import com.honatsugiexp.canvasegg.data.svg.parser.command.y2
import com.honatsugiexp.canvasegg.util.ColorSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import androidx.compose.ui.graphics.Color as ComposeColor

@Serializable
sealed class SvgPaint {
    @Serializable
    data object None: SvgPaint() {
        override fun toSvgString() = "none"
    }
    @Serializable
    data class Color(
        @Serializable(with = ColorSerializer::class)
        val color: ComposeColor,
        val colorString: String,
        val colorSpace: SvgColorSpace
    ): SvgPaint() {
        override fun toSvgString(): String = colorString
        companion object {
            fun fromString(colorString: String) =
                Color(
                    SvgColorParser.parse(colorString),
                    colorString,
                    SvgColorParser.colorSpace(colorString)
                )
            fun fromString(colorString: String, opacityString: String? = null): Color {
                var parsedColor = SvgColorParser.parse(colorString)

                if (opacityString != null) {
                    val alpha = opacityString.toFloatOrNull() ?: 1.0f
                    parsedColor = parsedColor.copy(alpha = alpha)
                }

                return Color(parsedColor, colorString, SvgColorParser.colorSpace(colorString))
            }
        }
    }
    sealed interface BrushPaint {
        fun toBrush(): Brush
    }
    @Serializable
    sealed class Gradient(
        @Transient open val colors: Map<SvgNormalizedFloat, Color> = emptyMap()
    ): SvgPaint(), BrushPaint
    @Serializable
    data class LinearGradient(
        override val colors: Map<SvgNormalizedFloat, Color>,
        val drawArea: SvgBoundingBox
    ): Gradient(colors) {
        override fun toSvgString(): String = ""

        override fun toBrush(): Brush = Brush.linearGradient(
            *colors.map { (key, color) ->
                key.value to color.color
            }.toTypedArray(),
            start = Offset(drawArea.x, drawArea.y),
            end = Offset(drawArea.width, drawArea.height)
        )
    }
    @Serializable
    data class RadialGradient(
        override val colors: Map<SvgNormalizedFloat, Color>,
        val transformInfo: TransformInfo
    ): Gradient(colors) {
        override fun toSvgString(): String = ""
        override fun toBrush(): Brush = Brush.radialGradient(
            *colors.map { (key, color) ->
                key.value to color.color
            }.toTypedArray(),
            center = Offset(transformInfo.fx, transformInfo.fy),
            radius = transformInfo.fr
        )
        data class TransformInfo(
            val cx: Float,
            val cy: Float,
            val r: Float,
            val fx: Float,
            val fy: Float,
            val fr: Float
        )
    }
    @Serializable
    data class Url(val id: String): SvgPaint() {
        override fun toSvgString(): String = "url($id)"
    }
    companion object {
        fun urlResolve(root: RootCommand, url: Url): SvgPaint {
            val id = url.id.removeSurrounding("\"")
            var result: SvgPaint = None
            root
                .childrenList()
                .asSequence()
                .filterIsInstance<DefCommand>()
                .forEach { command ->
                    result = when (command) {
                        is LinearGradientCommand -> command
                            .stops
                            .associate { stopCommand ->
                                stopCommand.offset to stopCommand.color
                            }
                            .let {
                                LinearGradient(
                                    colors = it,
                                    drawArea = SvgBoundingBox.fromValues(
                                        command.x1.toPxValue(command.lenEnv),
                                        command.y1.toPxValue(command.lenEnv),
                                        command.x2.toPxValue(command.lenEnv),
                                        command.y2.toPxValue(command.lenEnv)
                                    )
                                )
                            }

                        is RadialGradientCommand -> command
                            .stops
                            .associate { stopCommand ->
                                stopCommand.offset to stopCommand.color
                            }
                            .let {
                                RadialGradient(
                                    colors = it,
                                    transformInfo = RadialGradient.TransformInfo(
                                        command.cx.toPxValue(command.lenEnv),
                                        command.cy.toPxValue(command.lenEnv),
                                        command.r.toPxValue(command.lenEnv),
                                        command.fx.toPxValue(command.lenEnv),
                                        command.fy.toPxValue(command.lenEnv),
                                        command.fr.toPxValue(command.lenEnv),
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
            return when {
                value == null || value.equals("none", ignoreCase = true) -> None
                value.startsWith("url(") && value.endsWith(")") -> {
                    val url = value.removePrefix("url(").removeSuffix(")")
                    val id = url.removeSurrounding("\"").removeSurrounding("\'")
                    Url(id)
                }
                else -> {
                    Color.fromString(value)
                }
            }
        }
        fun resolve(value: String?): SvgPaint {
            return when {
                value == null || value.equals("none", ignoreCase = true) -> None
                value.startsWith("url(") && value.endsWith(")") -> {
                    val url = value.removePrefix("url(").removeSuffix(")")
                    val id = url.removePrefix("#")
                    Url(id)
                }
                else -> {
                    Color.fromString(value)
                }
            }
        }
    }

    abstract fun toSvgString(): String
}

internal fun SvgPaint.entity(command: RenderCommand) = when (this) {
    is SvgPaint.Url -> (command as? HasParentCommand)?.root()?.let {
        SvgPaint.urlResolve(it, this)
    } ?: SvgPaint.None
    else -> this
}