package com.honatsugiexp.canvasegg.pdf.util.data

import org.apache.pdfbox.cos.COSInteger

internal inline val Int.cosInteger
    get() = COSInteger.get(toLong())