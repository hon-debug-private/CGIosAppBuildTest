package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.common.util.attrOrNull
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgNormalizedFloat
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgPaint

class StopCommand(
    override val env: RenderEnv,
    override val parent: RenderCommand?,
    override val element: Element
): RenderCommand(env, parent),
    ElementCommand {
    override val styleData: ElementStyleData = styleData()
    val offset: SvgNormalizedFloat
        get() = SvgNormalizedFloat(
            element.attrOrNull("offset")
        )
    val color: SvgPaint.Color
        get() = SvgPaint.resolve(
            styleData.attrOrStyleOrNull("stop-color"),
            env
        ) as? SvgPaint.Color ?: SvgPaint.Color.Unknown
    val opacity: SvgNormalizedFloat
        get() = SvgNormalizedFloat(
            styleData.attrOrStyleOrNull("stop-opacity")?.toFloatOrNull() ?: 1f
        )
}