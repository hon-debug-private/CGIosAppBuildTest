package io.github.honatsugiexpress.canvasegg.data.svg.parser.command

import com.fleeksoft.ksoup.nodes.Element
import io.github.honatsugiexpress.canvasegg.data.css.ElementStyleData

class ScriptCommand(
    override val env: RenderEnv,
    override val parent: RenderCommand?,
    override val element: Element
): RenderCommand(env, parent), ElementCommand, DefCommand {
    override val styleData: ElementStyleData = styleData()
    fun runScript() {
        val body = element.text()
        println(body)
        env.scriptEngine?.runScript(body)
    }
}