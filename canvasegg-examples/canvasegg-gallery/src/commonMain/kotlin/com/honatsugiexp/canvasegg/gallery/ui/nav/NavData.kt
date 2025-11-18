package com.honatsugiexp.canvasegg.gallery.ui.nav

import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.honatsugiexp.canvasegg.gallery.data.Platform
import com.honatsugiexp.canvasegg.gallery.data.PlatformCheck
import com.honatsugiexp.canvasegg.gallery.ui.RootModel
import com.honatsugiexp.canvasegg.gallery.ui.screen.GalleryScreen
import com.honatsugiexp.canvasegg.gallery.ui.screen.StartScreen
import com.honatsugiexp.canvasegg.gallery.ui.screen.StartScreenTopAppBar
import com.honatsugiexp.canvasegg.gallery.ui.theme.CanvasEggGalleryTheme
import com.honatsugiexp.canvasegg.gallery.util.viewModelByOptionalOwner
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.stringResource
import kotlin.reflect.typeOf

class NavData {
    lateinit var navController: NavHostController
    lateinit var snackbarState: SnackbarHostState
    lateinit var currentRoute: String
    lateinit var scope: CoroutineScope
}

val LocalRootStore = compositionLocalOf<ViewModelStoreOwner?> {
    null
}

var currentOpenNavData: NavData = NavData()

val LocalNavData = compositionLocalOf<NavData> {
    error("NavData is not provided")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavInit(startDestination: String, noScaffold: Boolean = false) {
    CompositionLocalProvider(LocalRootStore provides LocalViewModelStoreOwner.current) {
        val rootModel = viewModelByOptionalOwner(LocalRootStore.current) {
            RootModel()
        }
        val navData = remember { NavData() }
        currentOpenNavData = navData
        CompositionLocalProvider(LocalNavData provides navData) {
            navData.apply {
                navController = rememberNavController()
                snackbarState = remember { SnackbarHostState() }
                val entryState by navController.currentBackStackEntryAsState()
                currentRoute = if (LocalInspectionMode.current) {
                    startDestination
                } else {
                    rootModel.currentDialogScreen
                        ?: entryState?.destination?.route
                        ?: NavRoutes.Start.name
                }
                scope = rememberCoroutineScope()
                CanvasEggGalleryTheme {
                    val navHost: @Composable (padding: PaddingValues) -> Unit = { padding ->
                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            modifier = Modifier.padding(padding),
                            enterTransition = {
                                slideIn { fullSize -> IntOffset(fullSize.width, 0) }
                            },
                            popEnterTransition = {
                                slideIn { fullSize -> IntOffset(-fullSize.width, 0) }
                            },
                            exitTransition = {
                                slideOut { fullSize -> IntOffset(-fullSize.width, 0) }
                            },
                            popExitTransition = {
                                slideOut { fullSize -> IntOffset(fullSize.width, 0) }
                            }
                        ) {
                            composable(NavRoutes.Start.name) {
                                StartScreen()
                            }
                            composable(NavRoutes.Gallery.name) {
                                GalleryScreen()
                            }
                        }
                    }
                    if (noScaffold) {
                        navHost(PaddingValues(0.dp))
                    } else {
                        Scaffold(
                            topBar = {
                                if (PlatformCheck.current != Platform.Desktop) {
                                    when (currentRoute) {
                                        NavRoutes.Start.name -> StartScreenTopAppBar()

                                        else -> {}
                                    }
                                }
                            },
                            snackbarHost = {
                                SnackbarHost(snackbarState)
                            }
                        ) { padding ->
                            navHost(padding)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getWindowSize(): IntSize {
    return LocalWindowInfo.current.containerSize
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun getWindowSizeClass(): WindowSizeClass {
    val windowSize = getWindowSize()
    val density = LocalDensity.current.density
    return WindowSizeClass.calculateFromSize(
        DpSize(
            windowSize.width.dp / density,
            windowSize.height.dp / density
        )
    )
}

enum class NavRoutes {
    Start,
    Gallery
}