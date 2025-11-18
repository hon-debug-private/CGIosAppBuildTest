package com.honatsugiexp.canvasegg.data.svg.type

import androidx.compose.ui.unit.dp
import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.HasParentCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.RenderCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.fontSize
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.height
import com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx.width
import com.honatsugiexp.canvasegg.data.svg.parser.command.parentList
import kotlinx.serialization.Serializable

@Serializable
sealed class SvgLength {
    @Serializable
    data object Unknown : SvgLength() {
        override val value: Float = 0f
        override val suffix: String = ""
        override fun toPx(env: Env): Px = Px(value)
    }

    @Serializable
    data class Px(override val value: Float) : SvgLength() {
        override val suffix = "px"
        override fun toPx(env: Env): Px = this
    }
    @Serializable
    data class In(override val value: Float) : SvgLength() {
        override val suffix = "in"

        override fun toPx(env: Env): Px = Px(value * env.dpi)
    }
    @Serializable
    data class Cm(override val value: Float) : SvgLength() {
        override val suffix = "cm"
        override fun toPx(env: Env): Px = Px(value * env.dpi / 2.54f)
    }
    @Serializable
    data class Pt(override val value: Float) : SvgLength() {
        override val suffix = "pt"
        override fun toPx(env: Env): Px = Px(value * env.dpi / 72f)
    }
    @Serializable
    data class Pc(override val value: Float) : SvgLength() {
        override val suffix = "pc"
        override fun toPx(env: Env): Px = Px(value * env.dpi / 6f)
    }
    @Serializable
    data class Mm(override val value: Float) : SvgLength() {
        override val suffix = "mm"
        override fun toPx(env: Env): Px = Px(value * env.dpi / 25.4f)
    }

    @Serializable
    data class Em(override val value: Float) : SvgLength() {
        override val suffix = "em"

        override val isRelative = true

        override fun toPx(env: Env) =
            Px(value * env.fontSize)
    }

    @Serializable
    data class Ex(override val value: Float) : SvgLength() {
        override val suffix = "ex"

        override val isRelative = true

        override fun toPx(env: Env) =
            Px(value * env.xHeight)
    }

    @Serializable
    data class Percent(override val value: Float) : SvgLength() {
        override val suffix = "%"

        override val isRelative = true

        override fun toPx(env: Env) =
            Px(value / 100f * env.referenceLength)
    }

    data class Env(
        val dpi: Float,
        val fontSize: Float,
        val xHeight: Float,
        val referenceLength: Float
    ) {
        companion object {
            fun newValue(
                oldLength: SvgLength,
                newFloat: Float,
                env: Env
            ): SvgLength = when (oldLength) {
                is Px -> Px(newFloat)

                is In -> In(newFloat / env.dpi)

                is Cm -> Cm(newFloat * 2.54f / env.dpi)

                is Pt -> Pt(newFloat * 72f / env.dpi)

                is Pc -> Pc(newFloat * 6f / env.dpi)

                is Mm -> Mm(newFloat * 25.4f / env.dpi)

                is Em -> Em(newFloat / env.fontSize)

                is Ex -> Ex(newFloat / env.xHeight)

                is Percent -> Percent(newFloat * 100f / env.referenceLength)

                Unknown -> Unknown
            }
            fun fromCommand(
                target: RenderCommand?,
                density: Float
            ): Env {
                val dpi = density * 160
                val stack = (target as? HasParentCommand)
                    ?.parentList()
                    ?.filterIsInstance<ElementCommand>()
                    ?: emptyList()

                var fontSize = 16f
                var referenceLength = 0f
                var xHeight = fontSize * 0.5f

                for (command in stack) {
                    command.fontSize()?.let { length ->
                        val envForCalc = Env(dpi, fontSize, xHeight, referenceLength)
                        fontSize = length.toPx(envForCalc).value
                        xHeight = fontSize * 0.5f
                    }
                    val refLength = command.width() ?: command.height()
                    if (refLength != null) {
                        val envForCalc = Env(dpi, fontSize, xHeight, referenceLength)
                        referenceLength = refLength.toPx(envForCalc).value
                    }
                }

                return Env(dpi, fontSize, xHeight, referenceLength)
            }
        }
    }

    abstract val value: Float
    abstract val suffix: String
    open val isRelative: Boolean = false
    abstract fun toPx(env: Env): Px
}

@Suppress("NOTHING_TO_INLINE")
inline fun SvgLength.toDp(env: SvgLength.Env) =
    toPx(env).value.dp

@Suppress("NOTHING_TO_INLINE")
inline fun SvgLength.toPxValue(env: SvgLength.Env) =
    toPx(env).value
@Suppress("NOTHING_TO_INLINE")
inline fun SvgLength.toScreenValue(env: SvgLength.Env, density: Float) =
    toPxValue(env) * density

@Suppress("NOTHING_TO_INLINE")
inline fun SvgLength.toSvgString() = value.toString() + suffix

fun SvgLength(value: String?): SvgLength {
    return when {
        value == null -> SvgLength.Unknown
        value.endsWith("px") -> {
            SvgLength.Px(value.removeSuffix("px").toFloat())
        }
        value.endsWith("in") -> {
            SvgLength.In(value.removeSuffix("in").toFloat())
        }
        value.endsWith("cm") -> {
            SvgLength.Cm(value.removeSuffix("cm").toFloat())
        }
        value.endsWith("pt") -> {
            SvgLength.Pt(value.removeSuffix("pt").toFloat())
        }
        value.endsWith("pc") -> {
            SvgLength.Pc(value.removeSuffix("pc").toFloat())
        }
        value.endsWith("mm") -> {
            SvgLength.Mm(value.removeSuffix("mm").toFloat())
        }
        value.endsWith("em") -> {
            SvgLength.Em(value.removeSuffix("em").toFloat())
        }
        value.endsWith("ex") -> {
            SvgLength.Ex(value.removeSuffix("ex").toFloat())
        }
        value.endsWith("%") -> {
            SvgLength.Percent(value.removeSuffix("%").toFloat())
        }
        value.toFloatOrNull() != null -> {
            SvgLength.Px(value.toFloat())
        }

        else -> {
            SvgLength.Unknown
        }
    }
}