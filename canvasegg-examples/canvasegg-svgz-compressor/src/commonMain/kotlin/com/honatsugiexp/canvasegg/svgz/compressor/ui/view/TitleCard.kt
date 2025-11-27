package com.honatsugiexp.canvasegg.svgz.compressor.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.Res
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.SvgToSvgz
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.svg_to_svgz_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TitleCard(
    imagePainter: Painter,
    text: String,
    onClick: () -> Unit = {}
) {
    OutlinedCard(
        Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = text,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}