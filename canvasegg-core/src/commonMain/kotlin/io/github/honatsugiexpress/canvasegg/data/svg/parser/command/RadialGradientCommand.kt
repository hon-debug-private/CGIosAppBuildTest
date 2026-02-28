package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength

class RadialGradientCommand(
    override val env: RenderEnv,
    stops: MutableList<StopCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): GradientCommand(env, stops, parent), DefCommand, ElementCommand {
    override val styleData: ElementStyleData = styleData()
}

inline val RadialGradientCommand.cx
    get() = if (element.hasAttr("cx")) {
        SvgLength(
            element.attr(
                "cx"
            )
        )
    } else SvgLength.Percent(50f)

inline val RadialGradientCommand.cy
    get() = if (element.hasAttr("cy")) {
        SvgLength(
            element.attr(
                "cy"
            )
        )
    } else SvgLength.Percent(50f)

inline val RadialGradientCommand.r
    get() = if (element.hasAttr("r")) {
        SvgLength(
            element.attr(
                "r"
            )
        )
    } else SvgLength.Percent(50f)

inline val RadialGradientCommand.fx
    get() = if (element.hasAttr("fx")) {
        SvgLength(
            (element.attr(
                "fx"
            ))
        )
    } else cx

inline val RadialGradientCommand.fy
    get() = if (element.hasAttr("fy")) {
        SvgLength(
            (element.attr(
                "fy"
            ))
        )
    } else cy

inline val RadialGradientCommand.fr
    get() = if (element.hasAttr("fr")) {
        SvgLength(
            (element.attr(
                "fr"
            ))
        )
    } else r