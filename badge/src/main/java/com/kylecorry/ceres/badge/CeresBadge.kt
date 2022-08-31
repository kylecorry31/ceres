package com.kylecorry.ceres.badge

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.kylecorry.andromeda.core.system.Resources

class CeresBadge(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var background: LinearLayout
    var statusText: TextView
    var statusImage: ImageView

    @ColorInt
    private var backgroundTint: Int = Color.WHITE

    @ColorInt
    private var foregroundTint: Int = Color.BLACK

    init {
        context.let {
            inflate(it, R.layout.ceres_badge, this)
            background = findViewById(R.id.ceres_badge)
            statusText = findViewById(R.id.ceres_badge_text)
            statusImage = findViewById(R.id.ceres_badge_image)
            val a = it.theme.obtainStyledAttributes(attrs, R.styleable.CeresBadge, 0, 0)
            backgroundTint = a.getColor(
                R.styleable.CeresBadge_backgroundTint,
                Resources.androidBackgroundColorSecondary(it)
            )
            foregroundTint = a.getColor(
                R.styleable.CeresBadge_foregroundTint,
                Resources.androidTextColorSecondary(it)
            )
            setImageResource(
                a.getResourceId(
                    R.styleable.CeresBadge_icon,
                    android.R.drawable.ic_dialog_info
                )
            )
            a.recycle()
        }
    }

    fun setImageResource(@DrawableRes resId: Int) {
        statusImage.setImageResource(resId)
        statusImage.imageTintList = ColorStateList.valueOf(foregroundTint)
    }

    fun setForegroundTint(@ColorInt color: Int) {
        statusImage.imageTintList = ColorStateList.valueOf(color)
        statusText.setTextColor(color)
        foregroundTint = color
    }

    fun setBackgroundTint(@ColorInt color: Int) {
        background.backgroundTintList = ColorStateList.valueOf(color)
        backgroundTint = color
    }

    fun setStatusText(text: String?) {
        if (text == null) {
            statusText.text = ""
            statusText.visibility = View.GONE
        } else {
            statusText.text = text
            statusText.visibility = View.VISIBLE
        }
    }


}