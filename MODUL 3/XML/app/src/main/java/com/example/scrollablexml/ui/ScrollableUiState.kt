package com.example.scrollablexml.ui

import com.example.scrollablexml.model.ScrollableData

data class ScrollableUiState(
    val list: List<ScrollableData> = emptyList(),
    val currentScrollableIndex: Int = 0,
    val selectedLocale: String = "en"
)
