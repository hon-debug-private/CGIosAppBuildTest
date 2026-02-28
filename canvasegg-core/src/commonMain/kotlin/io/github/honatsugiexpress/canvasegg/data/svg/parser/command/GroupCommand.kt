package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.Path
import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.TransformCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.TransformListCommand
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.applyCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.SvgTransformList

data class GroupCommand(
    override val env: RenderEnv,
    override val children: MutableList<RenderCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): ElementCommand, HasChildCommand(env, children, parent),
    HasTransformCommand,
    PathCommand {
    override val styleData: ElementStyleData = styleData()
    override fun transformCommands(): List<TransformCommand> {
        val transform = SvgTransformList.parse(styleData.attrOrStyleOrNull("transform"))
        val selfList = listOf(
            TransformListCommand(
                transform
            )
        )
        return selfList
    }

    override fun path(): Path {
        return Path().apply {
            parentListWithSelf()
                .asSequence()
                .filterIsInstance<HasTransformCommand>()
                .flatMap {
                    it.transformCommands()
                }
                .forEach {
                    applyCommand(it)
                }
            childrenList()
                .filterIsInstance<PathCommand>()
                .forEach {
                    addPath(it.path())
                }
        }
    }
}