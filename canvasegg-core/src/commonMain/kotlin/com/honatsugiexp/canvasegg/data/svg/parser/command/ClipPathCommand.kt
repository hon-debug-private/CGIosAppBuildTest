package com.honatsugiexp.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.Path
import com.fleeksoft.ksoup.nodes.Element
import com.honatsugiexp.canvasegg.common.util.attrOrNull
import com.honatsugiexp.canvasegg.data.svg.parser.command.transform.applyCommand
import com.honatsugiexp.cssparser.ElementStyleController

class ClipPathCommand(
    override val env: RenderEnv,
    override val children: MutableList<RenderCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): HasChildCommand(env, children, parent), DefCommand, ElementCommand, HasChildPathCommand {
    override val controller: ElementStyleController = controller()
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