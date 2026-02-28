package io.github.honatsugiexpress.canvasegg

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.get
import io.github.honatsugiexpress.canvasegg.ui.SvgCanvas
import kotlin.test.Test

class RenderCtxTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun renderGroup() = runComposeUiTest {
        setContent {
            val document = Ksoup.parseXml(
                """
                    <svg width="300px" height="300px" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg">
                        <g id="group-5">
                            <rect id="rect1" width="50px" height="30px" x="20px" y="5px" fill="red"/>
                            <rect id="rect1" width="50px" height="30px" x="30px" y="10px" fill="blue"/>
                        </g>
                    </svg>
                """.trimIndent()
            )
            val parser = SvgCanvasParser(document, SvgParserEnv.get())
            SvgCanvas(parser)
        }
    }
}