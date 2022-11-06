package com.kylecorry.ceres.chart.data

import androidx.annotation.ColorInt
import com.kylecorry.andromeda.canvas.ICanvasDrawer
import com.kylecorry.sol.math.Vector2
import com.kylecorry.ceres.chart.IChart

class ScatterChartLayer(
    initialData: List<Vector2>,
    @ColorInt initialColor: Int,
    val radius: Float = 6f,
    onPointClick: (point: Vector2) -> Boolean = { false }
) : BaseChartLayer(initialData, true, radius * 2, onPointClick) {


    @ColorInt
    var color: Int = initialColor
        set(value) {
            field = value
            invalidate()
        }

    override fun draw(drawer: ICanvasDrawer, chart: IChart) {
        drawer.noStroke()
        drawer.fill(color)
        val dpRadius = drawer.dp(radius)
        for (point in data) {
            val mapped = chart.toPixel(point)
            drawer.circle(mapped.x, mapped.y, dpRadius)
        }
        super.draw(drawer, chart)
    }
}