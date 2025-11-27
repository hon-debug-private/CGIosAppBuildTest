package com.honatsugiexp.canvasegg.svgz.compressor.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.Res
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.SvgToSvgz
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.WelcomeToApp
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.svg_to_svgz_image
import com.honatsugiexp.canvasegg.svgz.compressor.ui.nav.LocalNavController
import com.honatsugiexp.canvasegg.svgz.compressor.ui.nav.NavRoutes
import com.honatsugiexp.canvasegg.svgz.compressor.ui.view.TitleCard
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleCard(
            imagePainter = painterResource(Res.drawable.svg_to_svgz_image),
            text = stringResource(Res.string.SvgToSvgz),
            onClick = {
                navController.navigate(NavRoutes.Compressor.name)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(Res.string.WelcomeToApp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}