package com.example.scrollablemodul3.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scrollablemodul3.model.data.datastore.AppPreferencesRepository
import com.example.scrollablemodul3.model.data.remote.ApiResponse
import com.example.scrollablemodul3.model.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel(
    private val repository: MovieRepository,
    private val preferencesRepository: AppPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.selectedLocale.collect { locale ->
                val appLocale = mapLocale(locale)
                val category = _uiState.value.selectedCategory

                _uiState.value = _uiState.value.copy(
                    selectedLanguage = appLocale,
                    movies = emptyList(),
                    errorMessage = null
                )

                loadMovies(
                    category = category,
                    language = appLocale,
                    forceRefresh = false
                )
            }
        }

        viewModelScope.launch {
            preferencesRepository.selectedMovieCategory.collect { savedCategory ->
                if (_uiState.value.selectedCategory != savedCategory) {
                    _uiState.value = _uiState.value.copy(selectedCategory = savedCategory)
                }
                loadMovies(category = savedCategory)
            }
        }
    }

    fun mapLocale(locale: String) = when (locale) {
        "in" -> "id-ID"
        else -> "en-US"
    }

    private fun loadMovies(
        category: String = _uiState.value.selectedCategory,
        language: String = _uiState.value.selectedLanguage,
        forceRefresh: Boolean = false
    ) {
        viewModelScope.launch {
            val flow = when (category) {
                "now_playing" -> repository.getNowPlayingMovies(language, forceRefresh)
                "top_rated" -> repository.getTopRatedMovies(language, forceRefresh)
                else -> repository.getPopularMovies(language, forceRefresh)
            }

            flow.collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                    is ApiResponse.Success -> {
                        _uiState.value = _uiState.value.copy(
                            movies = response.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    is ApiResponse.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = response.message
                        )
                    }
                }
            }
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            movies = emptyList(),
            errorMessage = null
        )
        viewModelScope.launch {
            preferencesRepository.saveMovieCategory(category)
        }
        loadMovies(category)
    }

    fun refresh() {
        loadMovies(forceRefresh = true)
    }

    companion object {
        fun Factory(
            repository: MovieRepository,
            preferencesRepository: AppPreferencesRepository
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MovieViewModel(repository, preferencesRepository)
            }
        }
    }
}