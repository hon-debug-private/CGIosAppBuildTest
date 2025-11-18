package com.honatsugiexp.canvasegg.data.svg.parser.command.transform

import com.honatsugiexp.canvasegg.data.svg.type.SvgTransformList

data class TransformListCommand(val transformList: SvgTransformList): TransformCommand() {
    override fun toString(): String {
        return "TransformListCommand(transforms=$transformList)"
    }
}