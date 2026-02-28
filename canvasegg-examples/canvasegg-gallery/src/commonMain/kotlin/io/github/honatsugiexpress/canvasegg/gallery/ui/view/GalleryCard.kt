package io.github.honatsugiexpress.canvasegg.gallery.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canvasegg.canvasegg_examples.canvasegg_gallery.generated.resources.Res
import com.fleeksoft.ksoup.Ksoup
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgCanvasParser
import io.github.honatsugiexpress.canvasegg.data.svg.parser.SvgParserEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.get
import io.github.honatsugiexpress.canvasegg.gallery.data.GalleryData
import io.github.honatsugiexpress.canvasegg.ui.SvgCanvas
import org.jetbrains.compose.resources.stringResource

@Composable
fun GalleryCard(data: GalleryData, modifier: Modifier = Modifier) {
    val document = remember(data.svgData) {
        Ksoup.parseXml(data.svgData)
    }
    val env = SvgParserEnv.get()
    val parser = remember { SvgCanvasParser(document, env) }
    Card(modifier) {
        Column(Modifier.padding(8.dp)) {
            SvgCanvas(parser, Modifier.size(300.dp).align(Alignment.CenterHorizontally))
            HorizontalDivider()
            Text(
                text = stringResource(data.title),
                fontWeight = FontWeight.Medium,
                fontSize = 28.sp
            )
        }
    }
}