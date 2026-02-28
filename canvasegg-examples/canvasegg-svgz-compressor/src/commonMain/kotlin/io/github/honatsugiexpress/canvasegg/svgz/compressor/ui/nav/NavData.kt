package io.github.honatsugiexpress.canvasegg.svgz.compressor.ui.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.honatsugiexpress.canvasegg.svgz.compressor.ui.screen.CompressorScreen
import io.github.honatsugiexpress.canvasegg.svgz.compressor.ui.screen.CompressorTopBar
import io.github.honatsugiexpress.canvasegg.svgz.compressor.ui.screen.HomeScreen
import io.github.honatsugiexpress.canvasegg.svgz.compressor.ui.screen.HomeTopBar
import io.github.honatsugiexpress.canvasegg.svgz.compressor.ui.theme.CanvasEggSvgzCompressorTheme
import kotlinx.coroutines.CoroutineScope

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavData is not provided")
}

val LocalNavRoute = compositionLocalOf<String> {
    error("NavRoute is not provided")
}

val LocalRootScope = compositionLocalOf<CoroutineScope> {
    error("RootScope is not provided")
}

@Composable
fun NavInit(startDestination: String) {
    val navController = rememberNavController()
    val currentRoute = if (LocalInspectionMode.current) {
        startDestination
    } else {
        val entryState by navController.currentBackStackEntryAsState()
        entryState?.destination?.route ?: startDestination
    }
    CanvasEggSvgzCompressorTheme {
        CompositionLocalProvider(
            LocalNavController provides navController,
            LocalNavRoute provides currentRoute,
            LocalRootScope provides rememberCoroutineScope()
        ) {
            Scaffold(
                topBar = {
                    when (LocalNavRoute.current) {
                        NavRoutes.Home.name -> HomeTopBar()
                        NavRoutes.Compressor.name -> CompressorTopBar()
                    }
                }
            ) { padding ->
                NavHost(
                    navController,
                    startDestination,
                    Modifier.padding(padding)
                ) {
                    composable(NavRoutes.Home.name) {
                        HomeScreen()
                    }
                    composable(NavRoutes.Compressor.name) {
                        CompressorScreen()
                    }
                }
            }
        }
    }
}

enum class NavRoutes {
    Home,
    Compressor
}