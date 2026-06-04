package com.example.scrollablemodul3.ui.scrollable

import com.example.scrollablemodul3.model.data.local.entity.MovieEntity

data class ScrollableUiState(
    val movies: List<MovieEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: ErrorMessage = ErrorMessage.None,
    val currentItemIndex: Int = 0,
    val selectedLocale: String = "en",
    val selectedLanguage: String = "en-US"
)
