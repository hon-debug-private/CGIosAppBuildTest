package io.github.honatsugiexpress.canvasegg.pdf.util.data.paint

import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.graphics.color.PDColor
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB
import java.awt.Color

fun SvgPaint.toPDColor(env: SvgPDColorEnv): PDColor = when (this) {
    is SvgPaint.Color -> PDColor(
        floatArrayOf(color.red, color.green, color.blue, color.alpha),
        PDDeviceRGB.INSTANCE
    )
    is SvgPaint.LinearGradient -> toPDColor(env.resources)
    is SvgPaint.RadialGradient -> toPDColor(env.resources)
    else -> EmptyPDColor
}

data class SvgPDColorEnv(
    val resources: PDResources
)