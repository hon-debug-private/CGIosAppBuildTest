package io.github.honatsugiexpress.canvasegg.data.svg.type

import io.github.honatsugiexpress.canvasegg.common.util.doubleColor
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import androidx.compose.ui.graphics.Color as ComposeColor

object SvgColorParser {
    private fun toDegrees(radian: Float) = radian * (180f / PI.toFloat())
    private fun roundToOneDecimal(value: Float) = (value * 10).roundToInt() / 10f
    private fun fLab(t: Float) = if (t > 6f/29f) t else 3f*(6f/29f).pow(2f)*t + 4f/29f
    private fun inverseGammaCorrect(c: Float) = if (c <= 0.04045f) c / 12.92f else ((c + 0.055f) / 1.055f).pow(2.4f)
    private fun cubeRoot(c: Double) = if (c >= 0) c.pow(1.0/3.0) else -(-c).pow(1.0/3.0)

    fun colorSpace(colorString: String): SvgColorSpace {
        val str = colorString.trim().lowercase()
        return when {
            str == "none" -> SvgColorSpace.Unspecified
            CssColorKeywords.containsKey(str) -> SvgColorSpace.Keyword
            str.startsWith("#") -> SvgColorSpace.Hex
            str.startsWith("rgb") -> SvgColorSpace.Rgb
            str.startsWith("hsl") -> SvgColorSpace.Hsl
            str.startsWith("hwb") -> SvgColorSpace.Hwb
            str.startsWith("lab") -> SvgColorSpace.Lab
            str.startsWith("lch") -> SvgColorSpace.Lch
            str.startsWith("oklab") -> SvgColorSpace.Oklab
            str.startsWith("oklch") -> SvgColorSpace.Oklch
            str.startsWith("color(") -> colorSpaceByFunction(str)
            else -> SvgColorSpace.Unspecified
        }
    }

    private fun colorSpaceByFunction(colorString: String): SvgColorSpace {
        val content = colorString.substringAfter("color(").substringBefore(")")
        val parts = content.split(" ").filter { it.isNotBlank() }
        val space = parts.firstOrNull() ?: return SvgColorSpace.Unspecified
        return when(space.lowercase()) {
            "srgb" -> SvgColorSpace.Rgb
            "display-p3" -> SvgColorSpace.DisplayP3
            "rec2020" -> SvgColorSpace.Rec2020
            else -> SvgColorSpace.Unspecified
        }
    }

    fun parse(colorString: String): ComposeColor {
        val str = colorString.trim().lowercase()
        return when {
            str == "none" -> ComposeColor.Unspecified
            CssColorKeywords.containsKey(str) -> CssColorKeywords.getOrElse(str) {
                ComposeColor.Unspecified
            }
            str.startsWith("#") -> parseHex(str)
            str.startsWith("rgb") -> parseRgb(str)
            str.startsWith("hsl") -> parseHsl(str)
            str.startsWith("hwb") -> parseHwb(str)
            str.startsWith("lab") -> parseLab(str)
            str.startsWith("lch") -> parseLch(str)
            str.startsWith("oklab") -> parseOklab(str)
            str.startsWith("oklch") -> parseOklch(str)
            str.startsWith("color(") -> parseColorFunction(str)
            else -> ComposeColor.Unspecified
        }
    }

    private fun parseHex(str: String): ComposeColor {
        val hex = str.removePrefix("#")
        return when (hex.length) {
            3 -> {
                val r = hex[0].digitToInt(16) * 17
                val g = hex[1].digitToInt(16) * 17
                val b = hex[2].digitToInt(16) * 17
                ComposeColor(r / 255f, g / 255f, b / 255f)
            }
            6 -> {
                val r = hex.take(2).toInt(16)
                val g = hex.substring(2, 4).toInt(16)
                val b = hex.substring(4, 6).toInt(16)
                ComposeColor(r / 255f, g / 255f, b / 255f)
            }
            8 -> {
                val r = hex.take(2).toInt(16)
                val g = hex.substring(2, 4).toInt(16)
                val b = hex.substring(4, 6).toInt(16)
                val a = hex.substring(6, 8).toInt(16)
                ComposeColor(r / 255f, g / 255f, b / 255f, a / 255f)
            }
            else -> ComposeColor.Unspecified
        }
    }

    private fun parseRgb(str: String): ComposeColor {
        val nums = str.substringAfter("(").substringBefore(")")
            .split(",", " ")
            .filter { it.isNotBlank() }
            .map { it.trim() }
        val r = nums.getOrNull(0)?.toFloatOrNull() ?: 0f
        val g = nums.getOrNull(1)?.toFloatOrNull() ?: 0f
        val b = nums.getOrNull(2)?.toFloatOrNull() ?: 0f
        val a = nums.getOrNull(3)?.toFloatOrNull() ?: 1f
        return ComposeColor(r / 255f, g / 255f, b / 255f, a)
    }

    private fun parseHsl(str: String): ComposeColor {
        val nums = str.substringAfter("(").substringBefore(")")
            .split(",", " ", "/")
            .filter { it.isNotBlank() }
            .map { it.trim().removeSuffix("%") }

        val h = nums.getOrNull(0)?.toFloatOrNull() ?: 0f
        val s = nums.getOrNull(1)?.toFloatOrNull()?.div(100f) ?: 0f
        val l = nums.getOrNull(2)?.toFloatOrNull()?.div(100f) ?: 0f
        val a = nums.getOrNull(3)?.toFloatOrNull() ?: 1f

        val c = (1 - abs(2 * l - 1)) * s
        val x = c * (1 - abs((h / 60f) % 2 - 1))
        val m = l - c / 2
        val (r1, g1, b1) = when {
            h < 60 -> Triple(c, x, 0f)
            h < 120 -> Triple(x, c, 0f)
            h < 180 -> Triple(0f, c, x)
            h < 240 -> Triple(0f, x, c)
            h < 300 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }
        return ComposeColor(r1 + m, g1 + m, b1 + m, a)
    }

    private fun parseHwb(str: String): ComposeColor {
        val nums = str.substringAfter("(").substringBefore(")")
            .split(",", " ", "/")
            .filter { it.isNotBlank() }
            .map { it.trim().removeSuffix("%") }

        val h = nums.getOrNull(0)?.toFloatOrNull() ?: 0f
        val w = nums.getOrNull(1)?.toFloatOrNull()?.div(100f) ?: 0f
        val b = nums.getOrNull(2)?.toFloatOrNull()?.div(100f) ?: 0f
        val a = nums.getOrNull(3)?.toFloatOrNull() ?: 1f

        val rgbHsl = parseHsl("hsl($h,100%,50%)")
        val r = rgbHsl.red * (1 - w - b) + w
        val g = rgbHsl.green * (1 - w - b) + w
        val bl = rgbHsl.blue * (1 - w - b) + w
        return ComposeColor(r, g, bl, a)
    }
    private fun fInvLab(t: Float) = if (t > 6f/29f) t.pow(3f) else 3f*(6f/29f).pow(2f)*(t - 4f/29f)
    private fun gammaCorrect(c: Float) = if (c <= 0.0031308f) 12.92f*c else 1.055f*c.pow(1f/2.4f)-0.055f
    private fun cube(c: Double) = c.pow(3)
    private fun toRadians(degree: Float) = degree * (PI.toFloat() / 180f)
    fun parseLab(str: String): ComposeColor {
        val nums = str.substringAfter("(").substringBefore(")")
            .split(",", " ", "/").filter { it.isNotBlank() }.map { it.trim().toFloatOrNull() ?: 0f }
        val lightnessL = nums.getOrElse(0){0f}
        val greenToRedA = nums.getOrElse(1){0f}
        val blueToYellowB = nums.getOrElse(2){0f}
        val alpha = nums.getOrElse(3){1f}

        val y = (lightnessL + 16f) / 116f
        val x = greenToRedA / 500f + y
        val z = y - blueToYellowB / 200f
        val xyzX = fInvLab(x) * 0.95047f
        val xyzY = fInvLab(y)
        val xyzZ = fInvLab(z) * 1.08883f

        var r = xyzX * 3.2406f + xyzY * -1.5372f + xyzZ * -0.4986f
        var g = xyzX * -0.9689f + xyzY * 1.8758f + xyzZ * 0.0415f
        var b = xyzX * 0.0557f + xyzY * -0.2040f + xyzZ * 1.0570f

        r = gammaCorrect(r).coerceIn(0f,1f)
        g = gammaCorrect(g).coerceIn(0f,1f)
        b = gammaCorrect(b).coerceIn(0f,1f)
        return ComposeColor(r, g, b, alpha)
    }

    fun parseLch(str: String): ComposeColor {
        val nums = str.substringAfter("(").substringBefore(")")
            .split(",", " ", "/").filter { it.isNotBlank() }.map { it.trim().toFloatOrNull() ?: 0f }
        val lightnessL = nums.getOrElse(0){0f}
        val chromaC = nums.getOrElse(1){0f}
        val hueH = nums.getOrElse(2){0f}
        val alpha = nums.getOrElse(3){1f}
        val greenToRedA = chromaC * cos(toRadians(hueH))
        val blueToYellowB = chromaC * sin(toRadians(hueH))
        return parseLab("lab($lightnessL,$greenToRedA,$blueToYellowB,$alpha)")
    }

    fun parseOklab(str: String): ComposeColor {
        val nums = str.substringAfter("(").substringBefore(")")
            .split(",", " ", "/").filter { it.isNotBlank() }.map { it.trim().toFloatOrNull() ?: 0f }
        val lightness = nums.getOrElse(0){0f}
        val a = nums.getOrElse(1){0f}
        val b = nums.getOrElse(2){0f}
        val alpha = nums.getOrElse(3){1f}

        val lightnessL = lightness
        val aAxis = a
        val bAxis = b
        val lPrime = lightnessL + 0.3963377774 * aAxis + 0.2158037573 * bAxis
        val mPrime = lightnessL - 0.1055613458 * aAxis - 0.0638541728 * bAxis
        val sPrime = lightnessL - 0.0894841775 * aAxis - 1.2914855480 * bAxis

        val red = cube(+4.0767416621 * lPrime - 3.3077115913 * mPrime + 0.2309699292 * sPrime).coerceIn(0.0, 1.0)
        val green = cube(-1.2684380046 * lPrime + 2.6097574011 * mPrime - 0.3413193965 * sPrime).coerceIn(0.0, 1.0)
        val blue = cube(-0.0041960863 * lPrime - 0.7034186147 * mPrime + 1.7076147010 * sPrime).coerceIn(0.0, 1.0)
        return doubleColor(red, green, blue, alpha)
    }

    fun parseOklch(str: String): ComposeColor {
        val parts = str.substringAfter("(").substringBefore(")")
            .split(",", " ", "/")
            .filter { it.isNotBlank() }
        val nums = FloatArray(parts.size) { index ->
            parts[index].trim().toFloatOrNull() ?: 0f
        }
        val lightnessL = nums.getOrElse(0){0f}
        val chromaC = nums.getOrElse(1){0f}
        val hueH = nums.getOrElse(2){0f}
        val alpha = nums.getOrElse(3){1f}
        val a = chromaC * cos(toRadians(hueH))
        val b = chromaC * sin(toRadians(hueH))
        return parseOklab("oklab($lightnessL,$a,$b,$alpha)")
    }

    fun parseColorFunction(str: String): ComposeColor {
        val content = str.substringAfter("color(").substringBefore(")")
        val parts = content.split(" ", "/").filter { it.isNotBlank() }
        val space = parts.firstOrNull() ?: return ComposeColor.Unspecified
        val nums = parts.drop(1).map { it.toFloatOrNull() ?: 0f }
        val alpha = if (nums.size > 3) nums[3] else 1f
        return when(space.lowercase()) {
            "srgb","display-p3","rec2020" -> ComposeColor(
                nums.getOrElse(0){0f},
                nums.getOrElse(1){0f},
                nums.getOrElse(2){0f},
                alpha
            )
            else -> ComposeColor.Unspecified
        }
    }

    fun toColorString(color: ComposeColor, space: SvgColorSpace = SvgColorSpace.Rgb): String {
        return when (space) {
            SvgColorSpace.Rgb -> toRgb(color)
            SvgColorSpace.Hex -> toHex(color)
            SvgColorSpace.Hsl -> toHsl(color)
            SvgColorSpace.Hwb -> toHwb(color)
            SvgColorSpace.Lab -> toLab(color)
            SvgColorSpace.Lch -> toLch(color)
            SvgColorSpace.Oklab -> toOklab(color)
            SvgColorSpace.Oklch -> toOklch(color)
            SvgColorSpace.DisplayP3, SvgColorSpace.Rec2020 -> toColorFunction(color, space)
            else -> ""
        }
    }

    fun toHex(color: ComposeColor): String {
        val r = (color.red * 255).roundToInt().coerceIn(0, 255)
        val g = (color.green * 255).roundToInt().coerceIn(0, 255)
        val b = (color.blue * 255).roundToInt().coerceIn(0, 255)
        val a = (color.alpha * 255).roundToInt().coerceIn(0, 255)

        val hexRgb = "#${r.toHexString()}${g.toHexString()}${b.toHexString()}"

        return if (a < 255) {
            hexRgb + a.toHexString()
        } else {
            hexRgb
        }
    }

    fun toRgb(color: ComposeColor): String {
        val r = (color.red * 255).roundToInt().coerceIn(0, 255)
        val g = (color.green * 255).roundToInt().coerceIn(0, 255)
        val b = (color.blue * 255).roundToInt().coerceIn(0, 255)

        return if (color.alpha < 1f) {
            val a = roundToOneDecimal(color.alpha).coerceIn(0.0f, 1.0f)
            "rgb($r $g $b / $a)"
        } else {
            "rgb($r $g $b)"
        }
    }

    fun toHsl(color: ComposeColor): String {
        val r = color.red.coerceIn(0f, 1f)
        val g = color.green.coerceIn(0f, 1f)
        val b = color.blue.coerceIn(0f, 1f)

        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min

        var h = 0f
        var s = 0f
        val l = (max + min) / 2f

        if (delta != 0f) {
            s = if (l <= 0.5f) delta / (max + min) else delta / (2f - max - min)
            h = when (max) {
                r -> ((g - b) / delta) % 6f
                g -> ((b - r) / delta) + 2f
                b -> ((r - g) / delta) + 4f
                else -> 0f
            }
            h *= 60f
            if (h < 0) h += 360f
        }

        val hInt = h.roundToInt()
        val sPercent = (s * 100).roundToInt()
        val lPercent = (l * 100).roundToInt()

        return if (color.alpha < 1f) {
            val a = roundToOneDecimal(color.alpha).coerceIn(0.0f, 1.0f)
            "hsl($hInt $sPercent% $lPercent% / $a)"
        } else {
            "hsl($hInt $sPercent% $lPercent%)"
        }
    }

    fun toHwb(color: ComposeColor): String {
        val r = color.red.coerceIn(0f, 1f)
        val g = color.green.coerceIn(0f, 1f)
        val b = color.blue.coerceIn(0f, 1f)

        val hsl = toHsl(color)
        val h = hsl.substringAfter("(").substringBefore(" ")
            .toFloatOrNull() ?: 0f

        val w = minOf(r, g, b)
        val bl = 1f - maxOf(r, g, b)

        val wPercent = (w * 100).roundToInt()
        val bPercent = (bl * 100).roundToInt()

        return if (color.alpha < 1f) {
            val a = roundToOneDecimal(color.alpha).coerceIn(0.0f, 1.0f)
            "hwb(${h.roundToInt()} $wPercent% $bPercent% / $a)"
        } else {
            "hwb(${h.roundToInt()} $wPercent% $bPercent%)"
        }
    }

    fun toLab(color: ComposeColor): String {
        val rLinear = inverseGammaCorrect(color.red.coerceIn(0f, 1f))
        val gLinear = inverseGammaCorrect(color.green.coerceIn(0f, 1f))
        val bLinear = inverseGammaCorrect(color.blue.coerceIn(0f, 1f))

        var x = rLinear * 0.4124564f + gLinear * 0.3575761f + bLinear * 0.1804375f
        var y = rLinear * 0.2126729f + gLinear * 0.7151522f + bLinear * 0.0721750f
        var z = rLinear * 0.0193339f + gLinear * 0.1191920f + bLinear * 0.9503041f

        val refX = 0.95047f
        val refY = 1.0f
        val refZ = 1.08883f

        x /= refX
        y /= refY
        z /= refZ

        val fx = fLab(x)
        val fy = fLab(y)
        val fz = fLab(z)

        val lightnessL = 116f * fy - 16f
        val greenToRedA = 500f * (fx - fy)
        val blueToYellowB = 200f * (fy - fz)

        val l = roundToOneDecimal(lightnessL)
        val a = roundToOneDecimal(greenToRedA)
        val b = roundToOneDecimal(blueToYellowB)

        return if (color.alpha < 1f) {
            val alpha = roundToOneDecimal(color.alpha).coerceIn(0.0f, 1.0f)
            "lab($l $a $b / $alpha)"
        } else {
            "lab($l $a $b)"
        }
    }

    fun toLch(color: ComposeColor): String {
        val labStr = toLab(color)
        val parts = labStr.substringAfter("(").substringBefore(")").split(" ", "/")
            .filter { it.isNotBlank() }
        val lightnessL = parts.getOrElse(0){"0f"}.toFloatOrNull() ?: 0f
        val greenToRedA = parts.getOrElse(1){"0f"}.toFloatOrNull() ?: 0f
        val blueToYellowB = parts.getOrElse(2){"0f"}.toFloatOrNull() ?: 0f
        val alphaStr = parts.getOrElse(3){"1f"}

        val chromaC = sqrt(greenToRedA.pow(2) + blueToYellowB.pow(2))
        var hueH = toDegrees(atan2(blueToYellowB, greenToRedA))
        if (hueH < 0) hueH += 360f

        val l = roundToOneDecimal(lightnessL)
        val c = roundToOneDecimal(chromaC)
        val h = roundToOneDecimal(hueH)

        return if (color.alpha < 1f) {
            "lch($l $c $h / $alphaStr)"
        } else {
            "lch($l $c $h)"
        }
    }

    /**
     * ComposeColorをoklab(L, A, B[, A])形式の文字列に変換します。
     * (sRGB to LMS to Oklab 変換)
     */
    fun toOklab(color: ComposeColor): String {
        val rLinear = inverseGammaCorrect(color.red.coerceIn(0f, 1f)).toDouble()
        val gLinear = inverseGammaCorrect(color.green.coerceIn(0f, 1f)).toDouble()
        val bLinear = inverseGammaCorrect(color.blue.coerceIn(0f, 1f)).toDouble()

        val lPrime = cubeRoot(0.4121656120 * rLinear + 0.5362752080 * gLinear + 0.0515591800 * bLinear)
        val mPrime = cubeRoot(0.2118501800 * rLinear + 0.6807191700 * gLinear + 0.1074251700 * bLinear)
        val sPrime = cubeRoot(0.0883024619 * rLinear + 0.2818474174 * gLinear + 0.6298493407 * bLinear)

        val lightnessL = (0.2104542553 * lPrime + 0.7936177046 * mPrime - 0.0040719590 * sPrime).toFloat()
        val aAxis = (1.9779984951 * lPrime - 2.4285920367 * mPrime + 0.4505935416 * sPrime).toFloat()
        val bAxis = (0.0259040371 * lPrime + 0.7827717662 * mPrime - 0.8086757962 * sPrime).toFloat()

        val l = roundToOneDecimal(lightnessL)
        val a = roundToOneDecimal(aAxis)
        val b = roundToOneDecimal(bAxis)

        return if (color.alpha < 1f) {
            val alpha = roundToOneDecimal(color.alpha).coerceIn(0.0f, 1.0f)
            "oklab($l $a $b / $alpha)"
        } else {
            "oklab($l $a $b)"
        }
    }

    fun toOklch(color: ComposeColor): String {
        val oklabStr = toOklab(color)
        val parts = oklabStr.substringAfter("(").substringBefore(")").split(" ", "/")
            .filter { it.isNotBlank() }
        val lightnessL = parts.getOrElse(0){"0f"}.toFloatOrNull() ?: 0f
        val aAxis = parts.getOrElse(1){"0f"}.toFloatOrNull() ?: 0f
        val bAxis = parts.getOrElse(2){"0f"}.toFloatOrNull() ?: 0f
        val alphaStr = parts.getOrElse(3){"1f"}

        val chromaC = sqrt(aAxis.pow(2) + bAxis.pow(2))
        var hueH = toDegrees(atan2(bAxis, aAxis))
        if (hueH < 0) hueH += 360f

        val l = roundToOneDecimal(lightnessL)
        val c = roundToOneDecimal(chromaC)
        val h = roundToOneDecimal(hueH)

        return if (color.alpha < 1f) {
            "oklch($l $c $h / $alphaStr)"
        } else {
            "oklch($l $c $h)"
        }
    }

    fun toColorFunction(color: ComposeColor, space: SvgColorSpace): String {
        val spaceName = when (space) {
            SvgColorSpace.DisplayP3 -> "display-p3"
            SvgColorSpace.Rec2020 -> "rec2020"
            else -> "srgb"
        }

        val r = roundToOneDecimal(color.red.coerceIn(0f, 1f))
        val g = roundToOneDecimal(color.green.coerceIn(0f, 1f))
        val b = roundToOneDecimal(color.blue.coerceIn(0f, 1f))

        return if (color.alpha < 1f) {
            val a = roundToOneDecimal(color.alpha).coerceIn(0.0f, 1.0f)
            "color($spaceName $r $g $b / $a)"
        } else {
            "color($spaceName $r $g $b)"
        }
    }
}
