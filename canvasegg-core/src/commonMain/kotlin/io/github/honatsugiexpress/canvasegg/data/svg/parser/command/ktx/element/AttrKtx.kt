@file:Suppress("NOTHING_TO_INLINE")

package io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ktx.element

import io.github.honatsugiexpress.canvasegg.data.svg.parser.command.ElementCommand

inline fun ElementCommand.hasAttr(key: String) = styleData.attrOrStyleOrNull(key) != null

inline fun ElementCommand.attrOrStyleOrNull(key: String) = styleData.attrOrStyleOrNull(key)