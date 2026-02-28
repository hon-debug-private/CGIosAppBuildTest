package io.github.honatsugiexpress.canvasegg.ui.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.get
import io.github.honatsugiexpress.canvasegg.ui.SvgCanvas


@Preview(
    device = "spec:width=300px,height=300px,dpi=160,orientation=portrait,navigation=gesture",
    widthDp = 300,
    heightDp = 300,
    fontScale = 1f,
)
@Composable
private fun GradientRender() {
    val document = Ksoup.parseXml(
        """
            <svg width="300px" height="300px" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg">
                <defs>
                    <radialGradient id="gradient-1">
                        <stop offset="0%" stop-color="red"/>
                        <stop offset="25%" stop-color="blue"/>
                        <stop offset="50%" stop-color="green"/>
                        <stop offset="75%" stop-color="yellow"/>
                        <stop offset="100%" stop-color="white"/>
                    </radialGradient>
                    <linearGradient id="gradient-2">
                        <stop offset="0%" stop-color="red"/>
                        <stop offset="25%" stop-color="blue"/>
                        <stop offset="50%" stop-color="green"/>
                        <stop offset="75%" stop-color="yellow"/>
                        <stop offset="100%" stop-color="white"/>
                    </linearGradient>
                </defs>
                <rect id="rect1" width="50px" height="20px" x="20px" y="5px" fill="url(#gradient-1)"/>
                <rect id="rect2" width="50px" height="30px" x="40px" y="35px" fill="red"/>
                <rect id="rect3" width="50px" height="20px" x="60px" y="65px" fill="url(#gradient-2)"/>
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