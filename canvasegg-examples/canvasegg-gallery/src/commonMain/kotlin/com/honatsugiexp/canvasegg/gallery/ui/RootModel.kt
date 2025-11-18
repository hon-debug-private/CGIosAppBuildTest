package com.honatsugiexp.canvasegg.gallery.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RootModel: ViewModel() {
    var currentDialogScreen: String? by mutableStateOf(null)
}