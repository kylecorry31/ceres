package com.kylecorry.ceres.chart.data

import com.kylecorry.andromeda.canvas.ICanvasDrawer
import com.kylecorry.andromeda.core.units.PixelCoordinate
import com.kylecorry.sol.math.Vector2
import com.kylecorry.ceres.chart.IChart

abstract class BaseChartLayer(
    initialData: List<Vector2>,
    private val handleClicks: Boolean = true,
    private val pointClickRadiusDp: Float = 12f,
    private val onPointClick: (point: Vector2) -> Boolean = { false },
) : ChartLayer {

    override var data: List<Vector2> = initialData
        set(value) {
            field = value
            invalidate()
        }

    final override var hasChanges: Boolean = true
        private set

    override fun onClick(drawer: ICanvasDrawer, chart: IChart, pixel: PixelCoordinate): Boolean {
        if (!handleClicks) {
            return false
        }
        val radius = drawer.dp(pointClickRadiusDp)
        val clicked = data.map {
            val anchor = chart.toPixel(it)
            it to anchor.distanceTo(pixel)
        }
            .filter { it.second <= radius }
            .sortedBy { it.second }

        for (point in clicked) {
            if (onPointClick(point.first)) {
                return true
            }
        }
        return false
    }

    override fun draw(drawer: ICanvasDrawer, chart: IChart) {
        hasChanges = false
    }

    override fun invalidate() {
        hasChanges = true
    }
}