@file:Suppress("NOTHING_TO_INLINE")

package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength

class LinearGradientCommand(
    override val env: RenderEnv,
    stops: MutableList<StopCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): GradientCommand(env, stops, parent),
    DefCommand,
    ElementCommand {
    override val styleData: ElementStyleData = styleData()
}

inline val LinearGradientCommand.x1
    get() = if (element.hasAttr("x1")) {
        SvgLength(
            element.attr(
                "x1"
            )
        )
    } else SvgLength.Percent(0f)

inline val LinearGradientCommand.x2
    get() = if (element.hasAttr("x2")) {
        SvgLength(
            element.attr(
                "x2"
            )
        )
    } else SvgLength.Percent(100f)


inline val LinearGradientCommand.y1
    get() = if (element.hasAttr("y1")) {
        SvgLength(
            element.attr(
                "y1"
            )
        )
    } else SvgLength.Percent(0f)


inline val LinearGradientCommand.y2
    get() = if (element.hasAttr("y2")) {
        SvgLength(
            element.attr(
                "y2"
            )
        )
    } else SvgLength.Percent(0f)