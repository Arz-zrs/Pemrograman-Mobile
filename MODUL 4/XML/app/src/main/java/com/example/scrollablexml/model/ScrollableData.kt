package com.example.scrollablexml.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ScrollableData(
    val id: Int,
    @StringRes val titleResourceId: Int,
    @StringRes val subtitleResourceId: Int,
    @StringRes val descriptionResourceId: Int,
    @StringRes val detailResourceId: Int,
    @DrawableRes val imageResourceId: Int,
    @DrawableRes val detailImageResourceId: Int,
    val steamUrl: String
)
