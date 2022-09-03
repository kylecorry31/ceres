package com.kylecorry.ceres.list

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.andromeda.core.ui.Colors
import com.kylecorry.andromeda.core.ui.setCompoundDrawables
import kotlin.math.roundToInt

data class ListItem(
    val id: Long,
    val title: CharSequence,
    val subtitle: CharSequence? = null,
    val titleMaxLines: Int = Int.MAX_VALUE,
    val subtitleMaxLines: Int = Int.MAX_VALUE,
    val icon: ListIcon? = null,
    val checkbox: ListItemCheckbox? = null,
    val tags: List<ListItemTag> = emptyList(),
    val data: List<ListItemData> = emptyList(),
    val trailingText: CharSequence? = null,
    val trailingIcon: ListIcon? = null,
    val trailingIconAction: () -> Unit = {},
    val menu: List<ListMenuItem> = emptyList(),
    val longClickAction: () -> Unit = {},
    val action: () -> Unit = {}
)

data class ListMenuItem(val text: String, val action: () -> Unit)

data class ListItemTag(val text: String, val icon: ListIcon?, @ColorInt val color: Int)

data class ListItemData(val text: CharSequence, val icon: ListIcon?)

data class ListItemCheckbox(val checked: Boolean, val onClick: () -> Unit)

interface ListIcon {
    fun apply(image: ImageView)
    fun apply(text: TextView)
}

data class ResourceListIcon(
    @DrawableRes val id: Int,
    @ColorInt val tint: Int? = null,
    @DrawableRes val backgroundId: Int? = null,
    @ColorInt val backgroundTint: Int? = null,
    val foregroundScale: Float = 1f
) : ListIcon {
    override fun apply(image: ImageView) {
        // Default image width: 24dp
        image.isVisible = true
        image.setImageResource(id)
        Colors.setImageColor(image, tint)

        if (backgroundId != null) {
            image.setBackgroundResource(backgroundId)
            backgroundTint?.let {
                Colors.setImageColor(image.background, it)
            }
        } else {
            image.background = null
        }

        val padding = (1 - foregroundScale) * Resources.dp(image.context, 12f)
        image.setPadding(padding.roundToInt())
    }

    override fun apply(text: TextView) {
        text.setCompoundDrawables(
            Resources.dp(text.context, 12f).toInt(),
            left = id
        )
        Colors.setImageColor(text, tint)
        // TODO: Possibly apply background color
    }
}