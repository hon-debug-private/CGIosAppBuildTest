package com.honatsugiexp.canvasegg.imageio

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import ca.gosyer.appdirs.AppDirs
import com.fleeksoft.ksoup.Ksoup
import com.honatsugiexp.canvasegg.data.svg.parser.SvgParserEnv
import com.honatsugiexp.canvasegg.data.svg.parser.get
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileOutputStream
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ImageTest {
    private val testDir = AppDirs {
        appName = "CanvasEgg-Svg-Test-Suite-Test"
    }
    @OptIn(ExperimentalTestApi::class, ExperimentalTime::class)
    @Test
    fun pngImageTest1() = runComposeUiTest {
        setContent {
            val svgFile = """
                <svg width="300px" height="300px">
                    <rect width="100px" height="175px" x="30px" y="55px" fill="red"/>
                    <g transform="scale(2)">
                        <rect width="100px" height="175px" x="30px" y="55px" fill="red"/>
                    </g>
                </svg>
            """.trimIndent()
            val document = Ksoup.parseXml(svgFile)
            val env = SvgParserEnv.get()
            val imageIO = PngImageIO(document, env)
            val dir = File(testDir.getUserDataDir(), "imageio-result")
            dir.mkdirs()
            val file = File(dir, "${Clock.System.now().toEpochMilliseconds()}.png")
            val outputStream = FileOutputStream(file)
            imageIO.writeTo(outputStream.sink().buffer())
        }
    }
}