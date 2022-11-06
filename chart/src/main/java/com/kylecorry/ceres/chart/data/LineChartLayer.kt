package com.kylecorry.ceres.chart.data

import android.graphics.Path
import androidx.annotation.ColorInt
import com.kylecorry.andromeda.canvas.ICanvasDrawer
import com.kylecorry.sol.math.Vector2
import com.kylecorry.ceres.chart.IChart

class LineChartLayer(
    initialData: List<Vector2>,
    @ColorInt initialColor: Int,
    private val thickness: Float = 2f,
    onPointClick: (point: Vector2) -> Boolean = { false }
) : BaseChartLayer(initialData, true, onPointClick = onPointClick) {

    @ColorInt
    var color: Int = initialColor
        set(value) {
            field = value
            invalidate()
        }

    val path = Path()

    override fun draw(drawer: ICanvasDrawer, chart: IChart) {
        if (hasChanges) {
            path.rewind()
            for (i in 1 until data.size) {
                if (i == 1) {
                    val start = chart.toPixel(data[0])
                    path.moveTo(start.x, start.y)
                }

                val next = chart.toPixel(data[i])
                path.lineTo(next.x, next.y)
            }
        }

        drawer.noFill()
        drawer.strokeWeight(drawer.dp(thickness))
        drawer.stroke(color)
        drawer.path(path)

        super.draw(drawer, chart)
    }
}