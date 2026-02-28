package io.github.honatsugiexpress.canvasegg.pdf.util.data.paint

import io.github.honatsugiexpress.canvasegg.pdf.util.data.cosFloat
import org.apache.pdfbox.cos.COSArray
import org.apache.pdfbox.cos.COSDictionary
import org.apache.pdfbox.cos.COSFloat
import org.apache.pdfbox.cos.COSInteger
import org.apache.pdfbox.cos.COSName
import java.io.IOException


@Throws(IOException::class)
internal fun createType2FunctionDict(c0: FloatArray, c1: FloatArray): COSDictionary {
    val fdict = COSDictionary()
    fdict.setInt(COSName.FUNCTION_TYPE, 2)


    val domain = COSArray()
    domain.add(COSInteger.get(0))
    domain.add(COSInteger.get(1))
    fdict.setItem(COSName.DOMAIN, domain)

    val cosC0 = COSArray()
    for (f in c0) cosC0.add(f.cosFloat)
    fdict.setItem(COSName.C0, cosC0)

    val cosC1 = COSArray()
    for (f in c1) cosC1.add(f.cosFloat)
    fdict.setItem(COSName.C1, cosC1)

    fdict.setInt(COSName.N, 1)

    return fdict
}
