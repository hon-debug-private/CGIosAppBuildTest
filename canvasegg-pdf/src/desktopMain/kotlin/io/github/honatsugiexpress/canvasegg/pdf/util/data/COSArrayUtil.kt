package io.github.honatsugiexpress.canvasegg.pdf.util.data

import org.apache.pdfbox.cos.COSArray

internal val FloatArray.cosArray
    get() = COSArray().also { newArray ->
        forEach {
            newArray.add(it.cosFloat)
        }
    }