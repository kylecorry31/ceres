package com.kylecorry.ceres.chart.label

interface ChartLabelFormatter {
    fun format(value: Float): String
}