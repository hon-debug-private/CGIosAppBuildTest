package io.github.honatsugiexpress.canvasegg.gallery.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.ViewModelInitializer
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified T: ViewModel> viewModelByOptionalOwner(
    owner: ViewModelStoreOwner?,
    noinline initializer: CreationExtras.() -> T
): T {
    return if (LocalInspectionMode.current) {
        viewModel(initializer = initializer)
    } else {
        viewModel(owner!!, initializer = initializer)
    }
}