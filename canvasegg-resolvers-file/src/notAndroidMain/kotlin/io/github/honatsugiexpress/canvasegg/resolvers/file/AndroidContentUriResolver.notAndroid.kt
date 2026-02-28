package io.github.honatsugiexpress.canvasegg.resolvers.file

import androidx.compose.runtime.Composable
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.EmptyUriResolver
import io.github.honatsugiexpress.canvasegg.data.svg.resolver.uri.SvgUriResolver

actual class AndroidContentUriResolver actual constructor(
    context: Any?,
    actual override val baseUri: String,
    fontCopyFunction: FontCopyFunction
): SvgUriResolver

@Composable
@Suppress("ComposableNaming")
actual fun AndroidContentUriResolver(baseUri: String, fontCopyFunction: FontCopyFunction): SvgUriResolver = EmptyUriResolver