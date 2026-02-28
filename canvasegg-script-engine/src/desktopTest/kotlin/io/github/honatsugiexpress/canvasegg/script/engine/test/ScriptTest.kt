package io.github.honatsugiexpress.canvasegg.script.engine.test

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import ca.gosyer.appdirs.AppDirs
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.common.util.attrOrNull
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.get
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.toPxValue
import io.github.honatsugiexpress.canvasegg.script.engine.DefaultSvgScriptEngine
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.time.Clock

@OptIn(ExperimentalTestApi::class)
class ScriptTest {
    private val testDir = AppDirs {
        appName = "CanvasEgg-Test"
    }
    @Test
    fun simpleScriptTest1() = runComposeUiTest {
        setContent {
            val env = SvgParserEnv.get {
                scriptEngine = DefaultSvgScriptEngine.create()
            }
            val document = Ksoup.parseXml("""
                <svg width="300px" height="300px" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg">
                    <rect width="10px" height="10px" x="5px" y="15px" fill="red"/>
                    <script>
                        console.log("Log from JS");
                        window.custom = 30;
                        console.log(window.custom);
                    </script>
                </svg>
            """.trimIndent())
            val parser = SvgCanvasParser(document, env)
            val xLenEnv = SvgLength.Env.fromElement(
                document,
                density.density,
                SvgLength.Env.RefDirection.X
            )
            val yLenEnv = SvgLength.Env.fromElement(
                document,
                density.density,
                SvgLength.Env.RefDirection.Y
            )
            val width = SvgLength(document.attrOrNull("width") ?: "300px").toPxValue(xLenEnv)
            val height = SvgLength(document.attrOrNull("height") ?: "300px").toPxValue(yLenEnv)
            val image = ImageBitmap(width.roundToInt(), height.roundToInt())
            val canvas = Canvas(image)
            parser.draw(
                CanvasDrawScope().apply {
                    drawContext.canvas = canvas
                }
            )
            val dir = File(testDir.getUserDataDir(), "script-test")
            dir.mkdirs()
            val file = File(dir, "${Clock.System.now().toEpochMilliseconds()}.png")
            ImageIO.write(image.toAwtImage(), "png", file)
        }
    }
}