package com.example.scrollablemodul3.ui

import com.example.scrollablemodul3.model.ScrollableData

data class ScrollableUiState(
    val list: List<ScrollableData> = emptyList(),
    val currentScrollable: ScrollableData = ScrollableData(0, 0, 0, 0, 0, 0, ""),
    val currentScrollableIndex: Int = -1,
    val selectedLocale: String = "en"
)
