package io.github.honatsugiexpress.canvasegg.data.svg.type.font

import androidx.compose.ui.text.font.FontWeight
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ElementCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.RenderCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.parentList

private fun fontWeightIsAbsolute(value: String) = when {
    value.toIntOrNull()?.let {
        it in 1..1000
    } != null -> true
    value == "normal" -> true
    value == "bold" -> true
    value == "lighter" -> false
    value == "bolder" -> false
    else -> false
}

private fun mappingFontWeight(value: Int) = when (value) {
    in 0..150 -> FontWeight.W100
    in 151..250 -> FontWeight.W200
    in 251..350 -> FontWeight.W300
    in 351..450 -> FontWeight.W400
    in 451..550 -> FontWeight.W500
    in 551..650 -> FontWeight.W600
    in 651..750 -> FontWeight.W700
    in 751..850 -> FontWeight.W800
    in 851..1000 -> FontWeight.W900
    else -> FontWeight.W400
}

private fun nextFontWeight(value: Int, isBolder: Boolean): FontWeight {
    val parentWeight =
        mappingFontWeight(
            value
        )

    return if (isBolder) {
        when (parentWeight) {
            FontWeight.W100, FontWeight.W200, FontWeight.W300 -> FontWeight.W400
            FontWeight.W400, FontWeight.W500, FontWeight.W600 -> FontWeight.W700
            else -> FontWeight.W900
        }
    } else {
        when (parentWeight) {
            FontWeight.W500, FontWeight.W600, FontWeight.W700 -> FontWeight.W400
            FontWeight.W800, FontWeight.W900 -> FontWeight.W700
            else -> FontWeight.W100
        }
    }
}

fun stringToFontWeight(rawValue: String, command: RenderCommand): FontWeight {
    val value = rawValue.split("\\s".toRegex()).firstOrNull() ?: "400"
    return when {
        value.toIntOrNull() != null -> mappingFontWeight(
            value.toInt()
        )
        value == "normal" -> FontWeight.W400
        value == "bold" -> FontWeight.W700
        value == "lighter" -> command
            .parentList()
            .firstNotNullOfOrNull {
                if (it is ElementCommand && fontWeightIsAbsolute(
                        it.element.attr("font-weight")
                    )
                ) {
                    it.element.attr("font-weight")
                } else null
            }
            ?.let {
                val intValue = when (it) {
                    "normal" -> 400
                    "bold" -> 700
                    else -> it.toIntOrNull() ?: 400
                }
                nextFontWeight(
                    intValue,
                    false
                )
            } ?: FontWeight.W400

        value == "bolder" -> command
            .parentList()
            .firstNotNullOfOrNull {
                if (it is ElementCommand && fontWeightIsAbsolute(
                        it.element.attr("font-weight")
                    )
                ) {
                    it.element.attr("font-weight")
                } else null
            }
            ?.let {
                val intValue = when (it) {
                    "normal" -> 400
                    "bold" -> 700
                    else -> it.toIntOrNull() ?: 400
                }
                nextFontWeight(
                    intValue,
                    true
                )
            } ?: FontWeight.W400

        else -> FontWeight.W400
    }
}

fun absoluteStringToFontWeight(rawValue: String): FontWeight {
    val value = rawValue.split("\\s".toRegex()).firstOrNull() ?: "400"
    return when {
        value.toIntOrNull() != null -> mappingFontWeight(
            value.toInt()
        )
        value == "normal" -> FontWeight.W400
        value == "bold" -> FontWeight.W700
        else -> FontWeight.W400
    }
}