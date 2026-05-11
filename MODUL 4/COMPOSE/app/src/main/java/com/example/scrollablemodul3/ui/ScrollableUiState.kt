package com.example.scrollablemodul3.ui

import com.example.scrollablemodul3.model.ScrollableData

data class ScrollableUiState(
    val list: List<ScrollableData> = emptyList(),
    val currentItemIndex: Int = 0,
    val selectedLocale: String = "en"
)
