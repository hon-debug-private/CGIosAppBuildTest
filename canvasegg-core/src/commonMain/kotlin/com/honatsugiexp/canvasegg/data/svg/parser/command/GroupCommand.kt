package com.honatsugiexp.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.Path
import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.data.svg.parser.command.transform.TransformCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.transform.TransformListCommand
import com.honatsugiexp.canvasegg.data.svg.parser.command.transform.applyCommand
import com.honatsugiexp.canvasegg.data.svg.type.SvgTransformList
import com.honatsugiexp.canvasegg.data.svg.type.ktx.applySvgTransform
import com.honatsugiexp.cssparser.ElementStyleController

data class GroupCommand(
    override val env: RenderEnv,
    override val children: MutableList<RenderCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): ElementCommand, HasChildCommand(env, children, parent), HasTransformCommand, HasChildPathCommand {
    override val controller: ElementStyleController = controller()
    override fun transformCommands(): List<TransformCommand> {
        val controller = ElementStyleController(element)
        controller.parseDocumentStyles()
        val transform = SvgTransformList.parse(controller.attrOrStyleOrNull("transform"))
        val selfList = listOf(TransformListCommand(transform))
        return selfList
    }

    override fun childrenPath(): Path {
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