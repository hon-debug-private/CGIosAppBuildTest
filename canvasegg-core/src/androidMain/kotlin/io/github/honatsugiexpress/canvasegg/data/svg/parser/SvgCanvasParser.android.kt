package io.github.honatsugiexpress.canvasegg.data.svg.parser

import android.graphics.Path as AndroidPath
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.copy
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.hypot


actual fun Path.hitTest(x: Float, y: Float): Boolean {
    val newPath = copy().apply {
        transform(
            Matrix().apply { scale(100f, 100f) }
        )
    }
    val androidPath = newPath.asAndroidPath()
    val bounds = androidPath.computeBounds()
    val clip = Rect(
        floor(bounds.left.toDouble()).toInt(),
        floor(bounds.top.toDouble()).toInt(),
        ceil(bounds.right.toDouble()).toInt(),
        ceil(bounds.bottom.toDouble()).toInt()
    )
    val region = Region()
    region.setPath(androidPath, Region(clip))
    if (!region.isEmpty && region.contains((x * 100).toInt(), (y * 100).toInt())) {
        return true
    }
    val pm = PathMeasure(androidPath, false)
    val pos = FloatArray(2)
    val step = 2f
    var distance = 0f
    val tol = 5f * 100

    while (distance <= pm.length) {
        pm.getPosTan(distance, pos, null)
        val dx = pos[0] - x * 100f
        val dy = pos[1] - y * 100f
        if (hypot(dx, dy) <= tol) {
            return true
        }
        distance += step
    }

    return false
}

private fun AndroidPath.computeBounds(): RectF {
    val r = RectF()
    this.computeBounds(r, true)
    return r
}