package com.kylecorry.ceres.list

interface ListItemMapper<T> {
    fun map(value: T): ListItem
}