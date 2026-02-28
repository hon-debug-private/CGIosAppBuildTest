package io.github.honatsugiexpress.canvasegg.ui.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.get
import io.github.honatsugiexpress.canvasegg.ui.SvgCanvas

@Preview(widthDp = 300, heightDp = 300)
@Composable
private fun TransformRenderTest() {
    val document = Ksoup.parseXml(
        """
            <svg width="300px" height="300px" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg">
                <g id="group-5" transform="scale(1.2, 1.2) translate(10 20) translate(30, 50) scale(1.5)">
                    <rect id="rect1" width="50px" height="30px" x="20px" y="5px" fill="red"/>
                    <rect id="rect1" width="50px" height="30px" x="30px" y="10px" fill="blue"/>
                </g>
            </svg>
        """.trimIndent()
    )
    val env = SvgParserEnv.get()
    val parser =
        SvgCanvasParser(
            document,
            env
        )
    SvgCanvas(parser)
}