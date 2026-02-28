package io.github.honatsugiexpress.canvasegg.data.svg.type

import androidx.compose.ui.util.packFloats
import io.github.honatsugiexpress.canvasegg.util.unpackFirstFloat
import io.github.honatsugiexpress.canvasegg.util.unpackSecondFloat
import kotlin.jvm.JvmInline

sealed interface SvgTransform {
    @JvmInline
    value class Translate(val packedValue: Long): SvgTransform {
        constructor(x: Float, y: Float): this(
            packFloats(x, y)
        )

        override fun toString(): String {
            return "Translate(x=$x, y=$y)"
        }
        inline val x: Float
            get() = unpackFirstFloat(packedValue)
        inline val y: Float
            get() = unpackSecondFloat(packedValue)
    }
    @JvmInline
    value class Scale(val packedValue: Long): SvgTransform {
        constructor(x: Float, y: Float): this(
            packFloats(x, y)
        )

        override fun toString(): String {
            return "Scale(x=$x, y=$y)"
        }
        inline val x: Float
            get() = unpackFirstFloat(packedValue)
        inline val y: Float
            get() = unpackSecondFloat(packedValue)
    }
    @JvmInline
    value class Rotate(val packedArray: FloatArray): SvgTransform {
        constructor(angle: Float, x: Float, y: Float): this(
            floatArrayOf(
                angle,
                x,
                y
            )
        )

        override fun toString(): String {
            return "Rotate(angle=$angle, x=$x, y=$y)"
        }
        inline val angle: Float
            get() = packedArray[0]
        inline val x: Float
            get() = packedArray[1]
        inline val y: Float
            get() = packedArray[2]
    }
    @JvmInline
    value class SkewX(val angle: Float): SvgTransform
    @JvmInline
    value class SkewY(val angle: Float): SvgTransform
    @JvmInline
    value class Matrix(val packedArray: FloatArray): SvgTransform {
        val a: Float
            get() = packedArray[0]
        val b: Float
            get() = packedArray[1]
        val c: Float
            get() = packedArray[2]
        val d: Float
            get() = packedArray[3]
        val e: Float
            get() = packedArray[4]
        val f: Float
            get() = packedArray[5]

        override fun toString(): String {
            return "Matrix(a=$a, b=$b, c=$c, d=$d, e=$e, f=$f)"
        }
    }
    companion object {
        const val TRANSLATE_NAME = "translate"
        const val SCALE_NAME = "scale"
        const val ROTATE_NAME = "rotate"
        const val SKEW_X_NAME = "skewX"
        const val SKEW_Y_NAME = "skewY"
        const val MATRIX_NAME = "matrix"
        const val TRANSLATE_ID = 0
        const val SCALE_ID = 1
        const val ROTATE_ID = 2
        const val SKEW_X_ID = 3
        const val SKEW_Y_ID = 4
        const val MATRIX_ID = 5
        fun parse(value: String): SvgTransform? {
            val regex = "([a-z]+)\\(([^)]+)\\)".toRegex()
            val matchResult = regex.find(value.trim()) ?: return null
            val functionName = matchResult.groupValues[1]
            val argumentsRaw = matchResult.groupValues[2]
            val args = argumentsRaw.split("[\\s,]+".toRegex())
                .filter { it.isNotEmpty() }
            return when (functionName) {
                TRANSLATE_NAME -> {
                    val x = args.getOrNull(0)?.toFloatOrNull() ?: 0f
                    val y = args.getOrNull(1)?.toFloatOrNull() ?: 0f
                    Translate(x, y)
                }

                SCALE_NAME -> {
                    val x = args.getOrNull(0)?.toFloatOrNull() ?: 1f
                    val y = args.getOrNull(1)?.toFloatOrNull() ?: x
                    Scale(x, y)
                }

                ROTATE_NAME -> {
                    val angle = args.getOrNull(0)?.toFloatOrNull() ?: 0f
                    val x = args.getOrNull(1)?.toFloatOrNull() ?: 0f
                    val y = args.getOrNull(2)?.toFloatOrNull() ?: 0f
                    Rotate(angle, x, y)
                }

                SKEW_X_NAME -> {
                    val angle = args.getOrNull(0)?.toFloatOrNull() ?: 0f
                    SkewX(angle)
                }

                SKEW_Y_NAME -> {
                    val angle = args.getOrNull(0)?.toFloatOrNull() ?: 0f
                    SkewY(angle)
                }

                MATRIX_NAME -> {
                    val params = args.take(6).mapNotNull { it.toFloatOrNull() }.toFloatArray()

                    if (params.size != 6) {
                        return null
                    }

                    Matrix(params)
                }

                else -> null
            }
        }
    }
}