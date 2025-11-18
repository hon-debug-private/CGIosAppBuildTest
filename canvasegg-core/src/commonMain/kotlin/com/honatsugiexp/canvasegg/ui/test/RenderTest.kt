package com.honatsugiexp.canvasegg.ui.test

import androidx.compose.runtime.Composable
import com.fleeksoft.ksoup.Ksoup
import com.honatsugiexp.canvasegg.data.svg.parser.SvgCanvasParser
import com.honatsugiexp.canvasegg.data.svg.parser.SvgParserEnv
import com.honatsugiexp.canvasegg.data.svg.parser.get
import com.honatsugiexp.canvasegg.ui.SvgCanvas
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview(widthDp = 300, heightDp = 300)
@Composable
private fun StyledDocumentRender() {
    val document = Ksoup.parseXml(
        """
            <svg width="300px" height="300px" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg">
                <rect id="rect1" width="50px" height="30px" x="20px" y="5px" fill="red"/>
                <rect id="rect1" width="50px" height="30px" x="30px" y="10px" fill="blue"/>
            </svg>
        """.trimIndent()
    )
    val env = SvgParserEnv.get()
    val parser = SvgCanvasParser(document, env)
    SvgCanvas(document, parser)
}