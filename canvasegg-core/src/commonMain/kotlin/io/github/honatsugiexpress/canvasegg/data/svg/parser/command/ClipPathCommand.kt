package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import androidx.compose.ui.graphics.Path
import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.applyCommand

class ClipPathCommand(
    override val env: RenderEnv,
    override val children: MutableList<RenderCommand>,
    override val parent: RenderCommand?,
    override val element: Element
): HasChildCommand(env, children, parent), DefCommand, ElementCommand, PathCommand {
    override val styleData: ElementStyleData = styleData()
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