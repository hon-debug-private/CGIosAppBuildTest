package com.honatsugiexp.canvasegg.gallery.ui.nav

import kotlinx.browser.document

actual fun setWebTitle(title: String) {
    document.title = title
}