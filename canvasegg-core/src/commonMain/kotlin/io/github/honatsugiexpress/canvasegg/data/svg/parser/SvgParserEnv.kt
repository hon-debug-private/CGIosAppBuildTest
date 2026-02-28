package io.github.honatsugiexpress.canvasegg.data.svg.parser

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.script.SvgScriptEngine
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver

data class SvgParserEnv(
    val density: Density,
    val layoutDirection: LayoutDirection,
    val textMeasurer: TextMeasurer,
    private val deviceDarkMode: Boolean,
    val option: SvgParserOption
) {
    val isDarkMode
        get() = if (option.colorSchema == SvgColorSchema.Auto) {
            deviceDarkMode
        } else {
            option.colorSchema == SvgColorSchema.Dark
        }

    companion object
}

@Composable
fun SvgParserEnv.Companion.get(option: @Composable SvgParserOption.() -> Unit = {}) =
    SvgParserEnv(
        density = LocalDensity.current,
        layoutDirection = LocalLayoutDirection.current,
        textMeasurer = rememberTextMeasurer(),
        deviceDarkMode = isSystemInDarkTheme(),
        SvgParserOption()
            .apply { option() }
    )

fun SvgParserEnv.Companion.create(
    density: Density,
    layoutDirection: LayoutDirection,
    textMeasurer: TextMeasurer,
    deviceDarkMode: Boolean,
    option: SvgParserOption.() -> Unit = {}
) = SvgParserEnv(
    density,
    layoutDirection,
    textMeasurer,
    deviceDarkMode,
    SvgParserOption()
        .apply { option() }
)

class SvgParserOption {
    var drawArea: Path? = null
    var scaleX: Float = 1f
    var scaleY: Float = 1f
    val uriResolvers: MutableList<SvgUriResolver> = mutableListOf()
    var scriptEngine: SvgScriptEngine? = null
    var colorSchema: SvgColorSchema = SvgColorSchema.Auto
}