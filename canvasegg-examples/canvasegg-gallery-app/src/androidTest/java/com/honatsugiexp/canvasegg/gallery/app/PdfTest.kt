package com.honatsugiexp.canvasegg.gallery.app

import android.content.ContentValues
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.provider.MediaStore
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fleeksoft.ksoup.Ksoup
import com.honatsugiexp.canvasegg.data.svg.parser.SvgCanvasParser
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@RunWith(AndroidJUnit4::class)
class PdfTest {
    @OptIn(ExperimentalTime::class)
    @Test
    fun savePdf1() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val content = """
            <svg width="300px" height="300px">
                <rect width="150px" height="150px" fill="red"/>
                <rect width="150px" height="150px" x="150px" fill="blue"/>
                <rect width="150px" height="150px" y="150px" fill="green"/>
                <rect width="150px" height="150px" x="150px" y="150px" fill="yellow"/>
            </svg>
        """.trimIndent()
        val document = PdfDocument()
        val page0 = document.startPage(
            PdfDocument.PageInfo.Builder(
                (300 * context.resources.displayMetrics.density).roundToInt(),
                (300 * context.resources.displayMetrics.density).roundToInt(),
                0
            ).create()
        )
        val parser0 = SvgCanvasParser()
        val canvas0 = Canvas(page0.canvas)
        parser0.draw(
            newDrawScope = CanvasDrawScope().apply {
                drawContext.canvas = canvas0
            },
            newDensity = context.resources.displayMetrics.density,
            newDocument = Ksoup.parseXml(content)
        )
        document.finishPage(page0)
        val values = ContentValues().apply {
            put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/CanvasEggGallery")
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, "${Clock.System.now().toEpochMilliseconds()}.pdf")
        }
        val uri = context.contentResolver.insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), values)
        println(uri)
        uri?.let {
            context.contentResolver.openOutputStream(uri)?.use {
                println(it)
                document.writeTo(it)
            }
            document.close()
        }
    }
}