package com.example.scrollablemodul3.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scrollablemodul3.model.data.datastore.AppPreferencesRepository
import com.example.scrollablemodul3.model.data.remote.ApiResponse
import com.example.scrollablemodul3.model.data.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieViewModel(
    private val repository: MovieRepository,
    private val preferencesRepository: AppPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieUiState())
    private var fetchJob: Job? = null

    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                preferencesRepository.selectedLocale,
                preferencesRepository.selectedMovieCategory
            ) { locale, category ->
                val appLocale = mapLocale(locale)

                _uiState.update { it.copy(
                    selectedLanguage = appLocale,
                    selectedCategory = category,
                    movies = emptyList(),
                    isLoading = true,
                    errorMessage = ""
                ) }

                loadMovies(category = category, language = appLocale)
            }.collect {}
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
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            val flow = when (category) {
                "now_playing" -> repository.getNowPlayingMovies(language, forceRefresh)
                "top_rated" -> repository.getTopRatedMovies(language, forceRefresh)
                else -> repository.getPopularMovies(language, forceRefresh)
            }

            flow.collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = "") }
                    }
                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(
                            movies = response.data,
                            isLoading = false
                        ) }
                    }
                    is ApiResponse.Error -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            errorMessage = response.message ?: "Error"
                        ) }
                    }
                }
            }
        }
    }

    fun selectCategory(category: String, forceRefresh: Boolean = false) {
        _uiState.update { it.copy(
            selectedCategory = category,
            movies = emptyList(),
            isLoading = true,
            errorMessage = ""
        ) }
        viewModelScope.launch {
            preferencesRepository.saveMovieCategory(category)
        }
        loadMovies(category = category, forceRefresh = forceRefresh)
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