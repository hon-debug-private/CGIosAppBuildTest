@file:OptIn(ExperimentalTestApi::class)

package io.github.honatsugiexpress.canvaseeg.pdf.test

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import ca.gosyer.appdirs.AppDirs
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.get
import io.github.honatsugiexpress.canvasegg.pdf.PdfPageInfo
import io.github.honatsugiexpress.canvasegg.pdf.SvgToPdfConverter
import io.github.honatsugiexpress.canvasegg.pdf.of
import okio.buffer
import okio.sink
import java.io.File
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class PdfTest {
    private val testDir = AppDirs {
        appName = "CanvasEgg-Svg-Test-Suite-Test"
    }
    @OptIn(ExperimentalTime::class)
    @Test
    fun renderPdf1() = runComposeUiTest {
        setContent {
            val converter = SvgToPdfConverter.of()
            val info = PdfPageInfo.of(300, 300, 0)
            converter.startPage(info)
            val document = Ksoup.parseXml(
                """
                    <svg width="300px" height="300px" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg">
                        <rect id="rect1" width="50px" height="30px" x="20px" y="5px" fill="red"/>
                        <rect id="rect1" width="50px" height="30px" x="30px" y="10px" fill="blue"/>
                        <circle cx="100px" cy="100px" r="25px" fill="purple"/>
                        <ellipse cx="150px" cy="150px" rx="30px" ry="20px" fill="purple"/>
                    </svg>
                """.trimIndent()
            )
            val env = SvgParserEnv.get()
            converter.parse(document, env)
            converter.endPage()
            val dir = File(testDir.getUserDataDir()).resolve("pdf")
            dir.mkdirs()
            val file = File(dir, "${Clock.System.now().toEpochMilliseconds()}.pdf")
            file.sink().buffer().use {
                converter.writeTo(it)
            }
            converter.close()
        }
    }
}