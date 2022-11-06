package com.kylecorry.ceres.chart.data

import com.kylecorry.andromeda.canvas.ICanvasDrawer
import com.kylecorry.andromeda.core.units.PixelCoordinate
import com.kylecorry.sol.math.Vector2
import com.kylecorry.ceres.chart.IChart

interface ChartLayer {
    var data: List<Vector2>
    val hasChanges: Boolean

    fun draw(drawer: ICanvasDrawer, chart: IChart)

    /**
     * Called when the layer is clicked.
     * @return true if the click event was handled by this layer, false otherwise
     */
    fun onClick(drawer: ICanvasDrawer, chart: IChart, pixel: PixelCoordinate): Boolean

    fun invalidate()
}