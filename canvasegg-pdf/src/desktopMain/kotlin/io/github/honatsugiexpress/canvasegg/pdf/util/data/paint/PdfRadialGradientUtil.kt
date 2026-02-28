package io.github.honatsugiexpress.canvasegg.pdf.util.data.paint

import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint
import io.github.honatsugiexpress.canvasegg.pdf.util.data.cosFloat
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.graphics.color.PDColor
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading
import org.apache.pdfbox.pdmodel.graphics.shading.PDShadingType2
import org.apache.pdfbox.pdmodel.graphics.shading.PDShadingType3


fun SvgPaint.RadialGradient.toPDColor(resources: PDResources): PDColor {
    val function = createStitchingFunction(this)
    val coords = COSArray()
    coords.add(transformInfo.cx.cosFloat)
    coords.add(transformInfo.cy.cosFloat)
    coords.add(transformInfo.r.cosFloat)
    coords.add(transformInfo.fx.cosFloat)
    coords.add(transformInfo.fy.cosFloat)
    coords.add(transformInfo.fr.cosFloat)


    val colorShading = PDShadingType3(COSDictionary())
    colorShading.colorSpace = PDDeviceRGB.INSTANCE
    colorShading.shadingType = PDShading.SHADING_TYPE3
    colorShading.coords = coords
    colorShading.function = function
    val name = resources.add(colorShading)
    return PDColor(name, PDDeviceRGB.INSTANCE)
}