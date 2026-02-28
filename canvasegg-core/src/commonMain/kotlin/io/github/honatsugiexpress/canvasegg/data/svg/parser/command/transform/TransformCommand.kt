package io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawTransform
import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.transform.applyCommand
import io.github.honatsugiexpress.canvasegg.data.svg.type.util.ktx.applySvgTransform

sealed class TransformCommand

@Suppress("NOTHING_TO_INLINE")
@PublishedApi
internal inline fun DrawTransform.applyCommand(command: TransformCommand) = when (command) {
    is TransformListCommand -> applySvgTransform(command.transformList)
    is ClipPathTransformCommand -> clipPath(command.path)
}

@Suppress("NOTHING_TO_INLINE")
inline fun applyCommandToDrawTransform(drawTransform: DrawTransform, command: TransformCommand) = drawTransform.applyCommand(command)

@Suppress("NOTHING_TO_INLINE")
@PublishedApi
internal inline fun Path.applyCommand(command: TransformCommand) = when (command) {
    is TransformListCommand -> applySvgTransform(command.transformList)
    is ClipPathTransformCommand -> op(this, command.path, PathOperation.Intersect)
}

@Suppress("NOTHING_TO_INLINE")
inline fun applyCommandToPath(path: Path, command: TransformCommand) = path.applyCommand(command)