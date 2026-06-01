package com.example.scrollablemodul3.ui.movie

import com.example.scrollablemodul3.model.data.local.entity.MovieEntity

data class MovieUiState(
    val movies: List<MovieEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: ErrorMessage = ErrorMessage.None,
    val selectedCategory: String = "popular",
    val selectedLanguage: String = "en-US"
)