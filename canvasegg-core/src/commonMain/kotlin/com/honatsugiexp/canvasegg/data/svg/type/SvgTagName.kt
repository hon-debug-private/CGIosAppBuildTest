package com.honatsugiexp.canvasegg.data.svg.type

import com.fleeksoft.ksoup.nodes.Element
import kotlin.jvm.JvmInline

@JvmInline
value class SvgTagName private constructor(val name: String) {

    companion object {
        val ANCHOR = SvgTagName(ANCHOR_STRING)
        const val ANCHOR_STRING = "a"

        val ANIMATE = SvgTagName(ANIMATE_STRING)
        const val ANIMATE_STRING = "animate"

        val ANIMATE_MOTION = SvgTagName(ANIMATE_MOTION_STRING)
        const val ANIMATE_MOTION_STRING = "animateMotion"

        val ANIMATE_TRANSFORM = SvgTagName(ANIMATE_TRANSFORM_STRING)
        const val ANIMATE_TRANSFORM_STRING = "animateTransform"

        val CIRCLE = SvgTagName(CIRCLE_STRING)
        const val CIRCLE_STRING = "circle"

        val CLIP_PATH = SvgTagName(CLIP_PATH_STRING)
        const val CLIP_PATH_STRING = "clipPath"

        val DEFS = SvgTagName(DEFS_STRING)
        const val DEFS_STRING = "defs"

        val ELLIPSE = SvgTagName(ELLIPSE_STRING)
        const val ELLIPSE_STRING = "ellipse"

        val FE_BLEND = SvgTagName(FE_BLEND_STRING)
        const val FE_BLEND_STRING = "feBlend"

        val FE_COLOR_MATRIX = SvgTagName(FE_COLOR_MATRIX_STRING)
        const val FE_COLOR_MATRIX_STRING = "feColorMatrix"

        val FE_COMPONENT_TRANSFER = SvgTagName(FE_COMPONENT_TRANSFER_STRING)
        const val FE_COMPONENT_TRANSFER_STRING = "feComponentTransfer"

        val FE_COMPOSITE = SvgTagName(FE_COMPOSITE_STRING)
        const val FE_COMPOSITE_STRING = "feComposite"

        val FE_DIFFUSE_LIGHTING = SvgTagName(FE_DIFFUSE_LIGHTING_STRING)
        const val FE_DIFFUSE_LIGHTING_STRING = "feDiffuseLighting"

        val FE_DISPLACEMENT_MAP = SvgTagName(FE_DISPLACEMENT_MAP_STRING)
        const val FE_DISPLACEMENT_MAP_STRING = "feDisplacementMap"

        val FE_DISTANT_LIGHT = SvgTagName(FE_DISTANT_LIGHT_STRING)
        const val FE_DISTANT_LIGHT_STRING = "feDistantLight"

        val FE_FLOOD = SvgTagName(FE_FLOOD_STRING)
        const val FE_FLOOD_STRING = "feFlood"

        val FE_FUNC_A = SvgTagName(FE_FUNC_A_STRING)
        const val FE_FUNC_A_STRING = "feFuncA"

        val FE_FUNC_B = SvgTagName(FE_FUNC_B_STRING)
        const val FE_FUNC_B_STRING = "feFuncB"

        val FE_FUNC_G = SvgTagName(FE_FUNC_G_STRING)
        const val FE_FUNC_G_STRING = "feFuncG"

        val FE_FUNC_R = SvgTagName(FE_FUNC_R_STRING)
        const val FE_FUNC_R_STRING = "feFuncR"

        val FE_GAUSSIAN_BLUR = SvgTagName(FE_GAUSSIAN_BLUR_STRING)
        const val FE_GAUSSIAN_BLUR_STRING = "feGaussianBlur"

        val FE_IMAGE = SvgTagName(FE_IMAGE_STRING)
        const val FE_IMAGE_STRING = "feImage"

        val FE_MERGE = SvgTagName(FE_MERGE_STRING)
        const val FE_MERGE_STRING = "feMerge"

        val FE_MERGE_NODE = SvgTagName(FE_MERGE_NODE_STRING)
        const val FE_MERGE_NODE_STRING = "feMergeNode"

        val FE_MORPHOLOGY = SvgTagName(FE_MORPHOLOGY_STRING)
        const val FE_MORPHOLOGY_STRING = "feMorphology"

        val FE_OFFSET = SvgTagName(FE_OFFSET_STRING)
        const val FE_OFFSET_STRING = "feOffset"

        val FE_POINT_LIGHT = SvgTagName(FE_POINT_LIGHT_STRING)
        const val FE_POINT_LIGHT_STRING = "fePointLight"

        val FE_SPECULAR_LIGHTING = SvgTagName(FE_SPECULAR_LIGHTING_STRING)
        const val FE_SPECULAR_LIGHTING_STRING = "feSpecularLighting"

        val FE_SPOT_LIGHT = SvgTagName(FE_SPOT_LIGHT_STRING)
        const val FE_SPOT_LIGHT_STRING = "feSpotLight"

        val FE_TURBULENCE = SvgTagName(FE_TURBULENCE_STRING)
        const val FE_TURBULENCE_STRING = "feTurbulence"

        val FILTER = SvgTagName(FILTER_STRING)
        const val FILTER_STRING = "filter"

        val GROUP = SvgTagName(GROUP_STRING)
        const val GROUP_STRING = "g"

        val IMAGE = SvgTagName(IMAGE_STRING)
        const val IMAGE_STRING = "image"

        val LINE = SvgTagName(LINE_STRING)
        const val LINE_STRING = "line"

        val LINEAR_GRADIENT = SvgTagName(LINEAR_GRADIENT_STRING)
        const val LINEAR_GRADIENT_STRING = "linearGradient"

        val MASK = SvgTagName(MASK_STRING)
        const val MASK_STRING = "mask"

        val PATH = SvgTagName(PATH_STRING)
        const val PATH_STRING = "path"

        val PATTERN = SvgTagName(PATTERN_STRING)
        const val PATTERN_STRING = "pattern"

        val POLYGON = SvgTagName(POLYGON_STRING)
        const val POLYGON_STRING = "polygon"

        val POLYLINE = SvgTagName(POLYLINE_STRING)
        const val POLYLINE_STRING = "polyline"

        val RADIAL_GRADIENT = SvgTagName(RADIAL_GRADIENT_STRING)
        const val RADIAL_GRADIENT_STRING = "radialGradient"

        val RECT = SvgTagName(RECT_STRING)
        const val RECT_STRING = "rect"

        val STOP = SvgTagName(STOP_STRING)
        const val STOP_STRING = "stop"

        val SVG = SvgTagName(SVG_STRING)
        const val SVG_STRING = "svg"

        val TEXT = SvgTagName(TEXT_STRING)
        const val TEXT_STRING = "text"

        val TEXT_PATH = SvgTagName(TEXT_PATH_STRING)
        const val TEXT_PATH_STRING = "textPath"

        val TITLE = SvgTagName(TITLE_STRING)
        const val TITLE_STRING = "title"

        val TSPAN = SvgTagName(TSPAN_STRING)
        const val TSPAN_STRING = "tspan"

        val USE = SvgTagName(USE_STRING)
        const val USE_STRING = "use"

        val UNKNOWN = SvgTagName("")

        operator fun invoke(name: String) = when (name) {
            ANCHOR_STRING -> ANCHOR
            ANIMATE_STRING -> ANIMATE
            ANIMATE_MOTION_STRING -> ANIMATE_MOTION
            ANIMATE_TRANSFORM_STRING -> ANIMATE_TRANSFORM
            CIRCLE_STRING -> CIRCLE
            CLIP_PATH_STRING -> CLIP_PATH
            DEFS_STRING -> DEFS
            ELLIPSE_STRING -> ELLIPSE
            FE_BLEND_STRING -> FE_BLEND
            FE_COLOR_MATRIX_STRING -> FE_COLOR_MATRIX
            FE_COMPONENT_TRANSFER_STRING -> FE_COMPONENT_TRANSFER
            FE_COMPOSITE_STRING -> FE_COMPOSITE
            FE_DIFFUSE_LIGHTING_STRING -> FE_DIFFUSE_LIGHTING
            FE_DISPLACEMENT_MAP_STRING -> FE_DISPLACEMENT_MAP
            FE_DISTANT_LIGHT_STRING -> FE_DISTANT_LIGHT
            FE_FLOOD_STRING -> FE_FLOOD
            FE_FUNC_A_STRING -> FE_FUNC_A
            FE_FUNC_B_STRING -> FE_FUNC_B
            FE_FUNC_G_STRING -> FE_FUNC_G
            FE_FUNC_R_STRING -> FE_FUNC_R
            FE_GAUSSIAN_BLUR_STRING -> FE_GAUSSIAN_BLUR
            FE_IMAGE_STRING -> FE_IMAGE
            FE_MERGE_STRING -> FE_MERGE
            FE_MERGE_NODE_STRING -> FE_MERGE_NODE
            FE_MORPHOLOGY_STRING -> FE_MORPHOLOGY
            FE_OFFSET_STRING -> FE_OFFSET
            FE_POINT_LIGHT_STRING -> FE_POINT_LIGHT
            FE_SPECULAR_LIGHTING_STRING -> FE_SPECULAR_LIGHTING
            FE_SPOT_LIGHT_STRING -> FE_SPOT_LIGHT
            FE_TURBULENCE_STRING -> FE_TURBULENCE
            FILTER_STRING -> FILTER
            GROUP_STRING -> GROUP
            IMAGE_STRING -> IMAGE
            LINE_STRING -> LINE
            LINEAR_GRADIENT_STRING -> LINEAR_GRADIENT
            MASK_STRING -> MASK
            PATH_STRING -> PATH
            PATTERN_STRING -> PATTERN
            POLYGON_STRING -> POLYGON
            POLYLINE_STRING -> POLYLINE
            RADIAL_GRADIENT_STRING -> RADIAL_GRADIENT
            RECT_STRING -> RECT
            STOP_STRING -> STOP
            SVG_STRING -> SVG
            TEXT_STRING -> TEXT
            TEXT_PATH_STRING -> TEXT_PATH
            TITLE_STRING -> TITLE
            TSPAN_STRING -> TSPAN
            USE_STRING -> USE
            else -> UNKNOWN
        }

        operator fun invoke(element: Element) = invoke(element.tagName())
    }
}