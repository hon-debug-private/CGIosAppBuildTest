package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.common.util.attrOrNull
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.height
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.width
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.xLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element.yLenEnv
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgBoundingBox
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgLength
import io.github.honatsugiexpress.canvasegg.data.svg.type.toPxValue

class RootCommand(
    override val env: RenderEnv,
    override val children: MutableList<RenderCommand>,
    override val parent: RenderCommand?,
    override val element: Element,
    val defs: MutableList<DefCommand>
): HasChildCommand(env, children, parent), ViewportCommand {
    override val styleData: ElementStyleData = styleData()
    override fun boundingBox(): SvgBoundingBox {
        return if (element.hasAttr("viewBox")) {
            SvgBoundingBox.fromString(element.attr("viewBox"))
        } else SvgBoundingBox.fromValues(
            x = 0f,
            y = 0f,
            width = width?.toPxValue(xLenEnv) ?: 0f,
            height = height?.toPxValue(yLenEnv) ?: 0f
        )
    }

    override val fontSize: SvgLength
        get() = SvgLength(element.attrOrNull("font-size"))
}

inline val RootCommand.viewBox
    get() = ""