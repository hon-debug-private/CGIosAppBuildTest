package com.honatsugiexp.canvasegg.ui.test

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.fleeksoft.ksoup.Ksoup
import com.honatsugiexp.canvasegg.data.svg.parser.SvgCanvasParser
import com.honatsugiexp.canvasegg.data.svg.parser.SvgParserEnv
import com.honatsugiexp.canvasegg.data.svg.parser.get
import com.honatsugiexp.canvasegg.ui.SvgCanvas


@Preview(
    device = "spec:width=300px,height=300px,dpi=160,orientation=portrait,navigation=gesture",
    widthDp = 300,
    heightDp = 300,
    fontScale = 1f,
)
@Composable
private fun StyledDocumentRender() {
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
    val parser = SvgCanvasParser(document, env)
    SvgCanvas(parser)
}