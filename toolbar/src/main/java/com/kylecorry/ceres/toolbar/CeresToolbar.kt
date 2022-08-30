package com.kylecorry.ceres.toolbar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible

class CeresToolbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    val leftQuickAction: ImageButton
    val rightQuickAction: ImageButton
    val title: TextView
    val subtitle: TextView

    init {
        inflate(context, R.layout.view_ceres_toolbar, this)
        leftQuickAction = findViewById(R.id.ceres_toolbar_left_button)
        rightQuickAction = findViewById(R.id.ceres_toolbar_right_button)
        title = findViewById(R.id.ceres_toolbar_title)
        subtitle = findViewById(R.id.ceres_toolbar_subtitle)

        // Update attributes
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CeresToolbar, 0, 0)
        title.text = a.getString(R.styleable.CeresToolbar_title) ?: ""
        subtitle.text = a.getString(R.styleable.CeresToolbar_subtitle) ?: ""

        subtitle.isVisible = a.getBoolean(R.styleable.CeresToolbar_showSubtitle, true)

        val leftIcon = a.getResourceId(R.styleable.CeresToolbar_leftButtonIcon, -1)
        val rightIcon = a.getResourceId(R.styleable.CeresToolbar_rightButtonIcon, -1)

        if (leftIcon != -1) {
            leftQuickAction.isVisible = true
            leftQuickAction.setImageResource(leftIcon)
        }

        if (rightIcon != -1) {
            rightQuickAction.isVisible = true
            rightQuickAction.setImageResource(rightIcon)
        }


        val flattenQuickActions = a.getBoolean(R.styleable.CeresToolbar_flattenButtons, false)
        if (flattenQuickActions) {
            rightQuickAction.flatten()
            leftQuickAction.flatten()
        }
    }

    private fun ImageButton.flatten() {
        backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        elevation = 0f
    }

}