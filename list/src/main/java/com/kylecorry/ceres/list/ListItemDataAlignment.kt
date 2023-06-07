package com.kylecorry.ceres.list

import com.google.android.flexbox.AlignContent
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.JustifyContent

data class ListItemDataAlignment(
    @JustifyContent val horizontalSpacing: Int = JustifyContent.FLEX_START,
    @AlignItems val verticalAlignment: Int = AlignItems.FLEX_START,
    @AlignContent val horizontalAlignment: Int = AlignContent.FLEX_START
)
