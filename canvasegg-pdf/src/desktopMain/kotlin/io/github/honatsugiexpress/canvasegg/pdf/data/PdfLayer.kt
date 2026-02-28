package io.github.honatsugiexpress.canvasegg.pdf.data

import androidx.compose.ui.graphics.Paint
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject

internal data class PdfLayer(
    val form: PDFormXObject,
    val contentStream: PDPageContentStream,
    val paint: Paint
)