package com.honatsugiexp.canvasegg.data.svg.parser.command.transform

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawTransform
import com.honatsugiexp.canvasegg.data.svg.type.ktx.applySvgTransform

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