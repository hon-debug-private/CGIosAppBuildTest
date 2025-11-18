@file:Suppress("NOTHING_TO_INLINE")

package com.honatsugiexp.canvasegg.data.svg.parser.command.element.ktx

import com.honatsugiexp.canvasegg.data.svg.parser.command.ElementCommand

inline fun ElementCommand.hasAttr(key: String) = controller.attrOrStyleOrNull(key) != null

inline fun ElementCommand.attrOrStyleOrNull(key: String) = controller.attrOrStyleOrNull(key)