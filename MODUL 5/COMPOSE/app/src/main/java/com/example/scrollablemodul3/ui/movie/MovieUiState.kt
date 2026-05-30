package com.example.scrollablemodul3.ui.movie

import com.example.scrollablemodul3.model.data.local.entity.MovieEntity

data class MovieUiState(
    val movies: List<MovieEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val selectedCategory: String = "popular",
    val selectedLanguage: String = "en-US"
) {
    val hasError: Boolean get() = errorMessage.isNotBlank()
    val isInitialLoading: Boolean get() = movies.isEmpty() && isLoading
}
