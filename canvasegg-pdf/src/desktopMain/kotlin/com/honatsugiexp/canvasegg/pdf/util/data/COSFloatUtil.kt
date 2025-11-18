package com.honatsugiexp.canvasegg.pdf.util.data

import org.apache.pdfbox.cos.COSFloat

internal inline val Float.cosFloat
    get() = COSFloat(this)