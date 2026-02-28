package io.github.honatsugiexpress.canvasegg.pdf.util.data.paint

import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint
import io.github.honatsugiexpress.canvasegg.pdf.util.data.cosFloat
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSInteger
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.graphics.color.PDColor
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading
import org.apache.pdfbox.pdmodel.graphics.shading.PDShadingType2


fun SvgPaint.LinearGradient.toPDColor(resources: PDResources): PDColor {
    val function = createStitchingFunction(this)
    val coords = COSArray()
    coords.add(drawArea.x.cosFloat)
    coords.add(drawArea.y.cosFloat)
    coords.add(drawArea.width.cosFloat)
    coords.add(drawArea.height.cosFloat)


    val colorShading = PDShadingType2(COSDictionary())
    colorShading.colorSpace = PDDeviceRGB.INSTANCE
    colorShading.shadingType = PDShading.SHADING_TYPE2
    colorShading.coords = coords
    colorShading.function = function
    val name = resources.add(colorShading)
    return PDColor(name, PDDeviceRGB.INSTANCE)
}