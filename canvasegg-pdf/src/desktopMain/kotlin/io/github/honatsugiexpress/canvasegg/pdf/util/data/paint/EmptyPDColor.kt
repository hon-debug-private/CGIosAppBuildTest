package io.github.honatsugiexpress.canvasegg.pdf.util.data.paint

import org.apache.pdfbox.pdmodel.graphics.color.PDColor
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB

internal val EmptyPDColor = PDColor(
    floatArrayOf(0f, 0f, 0f),
    PDDeviceRGB.INSTANCE
)