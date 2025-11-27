package com.honatsugiexp.canvasegg.svgz.compressor.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.uri.Uri
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.CompressSvg
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.OpenSvg
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.Res
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.WelcomeToApp
import canvasegg.canvasegg_examples.canvasegg_svgz_compressor.generated.resources.arrow_back
import com.honatsugiexp.canvasegg.svgz.SvgzWriter
import com.honatsugiexp.canvasegg.svgz.compressor.data.util.getWriterFromSvg
import com.honatsugiexp.canvasegg.svgz.compressor.ui.nav.LocalNavController
import com.honatsugiexp.canvasegg.svgz.compressor.ui.nav.NavRoutes
import com.honatsugiexp.canvasegg.svgz.compressor.ui.view.OpenFilePickerButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CompressorScreen() {
    var currentUri: String? by remember { mutableStateOf(null) }
    val svgzWriter = currentUri?.let {
        getWriterFromSvg(it)
    }
    Column(Modifier.padding(8.dp)) {
        if (currentUri == null) {
            OpenFilePickerButton(
                onPick = { uri ->
                    currentUri = uri
                },
                mimeTypes = listOf("image/svg+xml")
            ) {
                Button(
                    onClick = it,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.OpenSvg))
                }
            }
        } else {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompressorTopBar() {
    val navController = LocalNavController.current
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(Res.string.CompressSvg))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack(NavRoutes.Compressor.name, true)
                }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back),
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}