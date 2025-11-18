package com.honatsugiexp.canvasegg.data.svg.type

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.PathParser
import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.common.util.attrOrNull
import com.honatsugiexp.canvasegg.data.svg.parser.command.ClipPathCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.DefCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.HasParentCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.LinearGradientCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RadialGradientCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RootCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.childrenList
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.attrOrStyleOrNull
import com.honatsugiexp.canvasegg.data.svg.parser.command.root
import com.honatsugiexp.canvasegg.data.svg.type.ktx.svgTagName
import com.honatsugiexp.canvasegg.util.ColorSerializer
import com.honatsugiexp.cssparser.ElementStyleController
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import androidx.compose.ui.graphics.Color as ComposeColor

@Serializable
sealed class SvgPaint {
    @Serializable
    data object None: SvgPaint() {
        override fun toSvgString() = "none"
        @Transient
        override val conversionObject = ConversionObject
        override fun convert(conversionObject: SvgPaint.ConversionObject): PaintConversionInfo {
            return when (conversionObject) {
                ConversionObject -> PaintConversionInfo(false, None)
                Color.ConversionObject -> PaintConversionInfo(
                    isSuccess = true,
                    result = Color(
                        color = ComposeColor.White,
                        colorString = "#ffffff",
                        colorSpace = SvgColorSpace.Hex
                    )
                )
                LinearGradient.ConversionObject -> PaintConversionInfo(
                    isSuccess = true,
                    result = LinearGradient(
                        mapOf(
                            SvgNormalizedFloat(0f) to Color(
                                color = ComposeColor.White,
                                colorString = "#ffffff",
                                colorSpace = SvgColorSpace.Hex
                            ),
                            SvgNormalizedFloat(1f) to Color(
                                color = ComposeColor(0x00FFFFFF),
                                colorString = "#ffffff00",
                                colorSpace = SvgColorSpace.Hex
                            )
                        )
                    )
                )
                RadialGradient.ConversionObject -> PaintConversionInfo(
                    isSuccess = true,
                    result = RadialGradient(
                        mapOf(
                            SvgNormalizedFloat(0f) to Color(
                                color = ComposeColor.White,
                                colorString = "#ffffff",
                                colorSpace = SvgColorSpace.Hex
                            ),
                            SvgNormalizedFloat(1f) to Color(
                                color = ComposeColor(0x00FFFFFF),
                                colorString = "#ffffff00",
                                colorSpace = SvgColorSpace.Hex
                            )
                        )
                    )
                )
            }
        }
        object ConversionObject: SvgPaint.ConversionObject
    }
    @Serializable
    data class Color(
        @Serializable(with = ColorSerializer::class)
        val color: ComposeColor,
        val colorString: String,
        val colorSpace: SvgColorSpace
    ): SvgPaint() {
        override fun toSvgString(): String = colorString
        @Transient
        override val conversionObject = ConversionObject
        object ConversionObject: SvgPaint.ConversionObject
        override fun convert(conversionObject: SvgPaint.ConversionObject): PaintConversionInfo {
            return when (conversionObject) {
                None.ConversionObject -> PaintConversionInfo(true, None)
                ConversionObject -> PaintConversionInfo(false, this)
                LinearGradient.ConversionObject -> PaintConversionInfo(
                    isSuccess = true,
                    result = LinearGradient(
                        mapOf(
                            SvgNormalizedFloat(0f) to this,
                            SvgNormalizedFloat(1f) to copy(color = color.copy(alpha = 0f))
                        )
                    )
                )

                RadialGradient.ConversionObject -> PaintConversionInfo(
                    isSuccess = true,
                    result = RadialGradient(
                        mapOf(
                            SvgNormalizedFloat(0f) to this,
                            SvgNormalizedFloat(1f) to copy(color = color.copy(alpha = 0f))
                        )
                    )
                )
            }
        }
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
    @Serializable
    sealed class Gradient(
        @Transient open val colors: Map<SvgNormalizedFloat, Color> = emptyMap()
    ): SvgPaint()
    @Serializable
    data object EmptyGradient: Gradient(emptyMap()) {
        override fun toSvgString(): String = ""
        override fun convert(conversionObject: ConversionObject): PaintConversionInfo {
            return PaintConversionInfo(false, EmptyGradient)
        }
    }
    @Serializable
    data class LinearGradient(override val colors: Map<SvgNormalizedFloat, Color>): Gradient(colors) {
        override fun toSvgString(): String = ""
        @Transient
        override val conversionObject = ConversionObject
        object ConversionObject: SvgPaint.ConversionObject
        override fun convert(conversionObject: SvgPaint.ConversionObject): PaintConversionInfo {
            return when (conversionObject) {
                None.ConversionObject -> PaintConversionInfo(true, None)
                Color.ConversionObject -> PaintConversionInfo(
                    isSuccess = true,
                    result = colors.firstNotNullOf { it.value }
                )
                ConversionObject -> PaintConversionInfo(false, this)

                RadialGradient.ConversionObject -> PaintConversionInfo(true, RadialGradient(colors))
            }
        }
    }
    @Serializable
    data class RadialGradient(override val colors: Map<SvgNormalizedFloat, Color>): Gradient(colors) {
        override fun toSvgString(): String = ""
        @Transient
        override val conversionObject = ConversionObject
        object ConversionObject: SvgPaint.ConversionObject
        override fun convert(conversionObject: SvgPaint.ConversionObject): PaintConversionInfo {
            return when (conversionObject) {
                None.ConversionObject -> PaintConversionInfo(true, None)
                Color.ConversionObject -> PaintConversionInfo(
                    isSuccess = true,
                    result = colors.firstNotNullOf { it.value }
                )
                LinearGradient.ConversionObject -> PaintConversionInfo(false, LinearGradient(colors))

                ConversionObject -> PaintConversionInfo(true, this)
            }
        }
    }
    @Serializable
    data class Url(val id: String): SvgPaint() {
        override fun toSvgString(): String = "url($id)"
        override fun convert(conversionObject: ConversionObject): PaintConversionInfo {
            return PaintConversionInfo(false, this)
        }
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
                                LinearGradient(it)
                            }

                        is RadialGradientCommand -> command
                            .stops
                            .associate { stopCommand ->
                                stopCommand.offset to stopCommand.color
                            }
                            .let {
                                RadialGradient(it)
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
                    val id = url.removePrefix("#")
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
    sealed interface ConversionObject

    abstract fun toSvgString(): String
    abstract fun convert(conversionObject: ConversionObject): PaintConversionInfo
    open val conversionObject: ConversionObject? = null
    data class PaintConversionInfo(
        val isSuccess: Boolean,
        val result: SvgPaint
    )
}

fun SvgPaint.Gradient.toBrush(rect: Rect): Brush {
    return when (this) {
        is SvgPaint.LinearGradient -> {
            Brush.linearGradient(
                *colors.map { (key, color) ->
                    key.value to color.color
                }.toTypedArray(),
                start = rect.topLeft,
                end = rect.bottomRight
            )
        }

        is SvgPaint.RadialGradient -> {
            Brush.radialGradient(
                *colors.map { (key, color) ->
                    key.value to color.color
                }.toTypedArray()
            )
        }

        is SvgPaint.EmptyGradient -> {
            SolidColor(ComposeColor.Unspecified)
        }
    }
}

fun SvgPaint.entity(command: RenderCommand) = when (this) {
    is SvgPaint.Url -> (command as? HasParentCommand)?.root()?.let {
        SvgPaint.urlResolve(it, this)
    } ?: SvgPaint.None
    else -> this
}