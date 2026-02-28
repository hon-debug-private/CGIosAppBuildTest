package io.github.honatsugiexpress.canvasegg.gallery.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canvasegg.canvasegg_examples.canvasegg_gallery.generated.resources.OpenGallery
import canvasegg.canvasegg_examples.canvasegg_gallery.generated.resources.Res
import canvasegg.canvasegg_examples.canvasegg_gallery.generated.resources.WelcomeAppTitle
import canvasegg.canvasegg_examples.canvasegg_gallery.generated.resources.photo_library
import io.github.honatsugiexpress.canvasegg.gallery.ui.nav.LocalNavData
import io.github.honatsugiexpress.canvasegg.gallery.ui.nav.NavRoutes
import io.github.honatsugiexpress.canvasegg.gallery.ui.nav.setWebTitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StartScreen() {
    val navData = LocalNavData.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val centerGradationColor = if (isSystemInDarkTheme()) {
            Color(0xFF044949)
        } else {
            Color(0xFF0FA3A3)
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(80.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            centerGradationColor,
                            MaterialTheme.colorScheme.primary
                        )
                    ),
                    shape = RoundedCornerShape(20)
                )
                .clickable {
                    navData.navController.navigate(NavRoutes.Gallery.name)
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(Res.drawable.photo_library),
                null,
                Modifier.size(44.dp),
                tint = MaterialTheme.colorScheme.surface
            )
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(Res.string.OpenGallery),
                color = MaterialTheme.colorScheme.surface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreenTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            setWebTitle(stringResource(Res.string.WelcomeAppTitle))
            Text(stringResource(Res.string.WelcomeAppTitle))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            scrolledContainerColor = Color.Unspecified,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = Color.Unspecified
        )
    )
}