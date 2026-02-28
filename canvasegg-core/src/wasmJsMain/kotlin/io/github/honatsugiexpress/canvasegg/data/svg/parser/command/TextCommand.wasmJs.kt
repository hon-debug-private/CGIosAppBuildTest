package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.fontFamily
import org.jetbrains.skia.Font
import org.jetbrains.skia.FontMgr
import org.jetbrains.skia.FontStyle

internal actual fun TextCommand.nativePath(): Path {
    val typeface = fontFamily.firstNotNullOfOrNull { font ->
        FontMgr.default.matchFamilyStyle(font, FontStyle.NORMAL)
    }
    val font = Font(typeface)
    val text = element.text()
    val glyphs = font.getStringGlyphs(text)
    val paths = font.getPaths(glyphs)
    return Path().apply {
        paths.forEach {
            addPath(it.asComposePath())
        }
    }
}