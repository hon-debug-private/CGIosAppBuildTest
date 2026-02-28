package io.github.honatsugiexpress.canvasegg.pdf.util.data.paint

import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint
import io.github.honatsugiexpress.canvasegg.data.svg.type.colors
import io.github.honatsugiexpress.canvasegg.data.svg.type.offsets
import io.github.honatsugiexpress.canvasegg.pdf.util.data.cosFloat
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSFloat
import org.apache.pdfbox.cos.COSInteger
import org.apache.pdfbox.cos.COSName
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.function.PDFunctionType2
import org.apache.pdfbox.pdmodel.common.function.PDFunctionType3
import java.io.IOException


@Throws(IOException::class)
internal fun createStitchingFunction(gradient: SvgPaint.Gradient): PDFunctionType3 {
    val intervalCount = gradient.stops.size - 1

    val functionsArray = COSArray()

    for (i in 0..<intervalCount) {
        val color = gradient.colors[i].color
        val nextColor = gradient.colors[i + 1].color
        val fdict: COSDictionary = createType2FunctionDict(
            floatArrayOf(
                color.red,
                color.green,
                color.blue
            ),
            floatArrayOf(
                nextColor.red,
                nextColor.green,
                nextColor.blue
            )
        )
        val subFunction = PDFunctionType2(fdict)

        functionsArray.add(subFunction.cosObject)
    }

    val fdict3 = COSDictionary()
    fdict3.setInt(COSName.FUNCTION_TYPE, 3)
    fdict3.setItem(COSName.FUNCTIONS, functionsArray)

    val domain = COSArray()
    domain.add(COSInteger.get(0))
    domain.add(COSInteger.get(1))
    fdict3.setItem(COSName.DOMAIN, domain)

    val bounds = COSArray()
    for (i in 1..<intervalCount) {
        bounds.add(gradient.offsets[i].value.cosFloat)
    }
    fdict3.setItem(COSName.BOUNDS, bounds)

    val encode = COSArray()
    repeat(intervalCount + 1) {
        encode.add(COSInteger.get(0))
        encode.add(COSInteger.get(1))
    }
    fdict3.setItem(COSName.ENCODE, encode)

    val range = COSArray()
    range.add(COSInteger.get(0))
    range.add(COSInteger.get(1))
    range.add(COSInteger.get(0))
    range.add(COSInteger.get(1))
    range.add(COSInteger.get(0))
    range.add(COSInteger.get(1))
    fdict3.setItem(COSName.RANGE, range)

    return PDFunctionType3(fdict3)
}