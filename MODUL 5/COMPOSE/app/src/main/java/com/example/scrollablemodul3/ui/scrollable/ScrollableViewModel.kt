package com.example.scrollablemodul3.ui.scrollable

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scrollablemodul3.R
import com.example.scrollablemodul3.model.data.datastore.AppPreferencesRepository
import com.example.scrollablemodul3.model.data.remote.ApiResponse
import com.example.scrollablemodul3.model.data.remote.ErrorType
import com.example.scrollablemodul3.model.data.repository.IMovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ScrollableViewModel(
    initialLocale: String,
    private val repository: IMovieRepository,
    private val preferencesRepository: AppPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScrollableUiState(selectedLocale = initialLocale))
    val uiState: StateFlow<ScrollableUiState> = _uiState.asStateFlow()
    private var fetchJob: Job? = null

    init {
        viewModelScope.launch {
            preferencesRepository.selectedLocale.collect { locale ->
                val appLocale = mapLocale(locale)
                val isNewLanguage = appLocale != _uiState.value.selectedLanguage

                _uiState.update { it.copy(
                    selectedLocale = locale,
                    selectedLanguage = appLocale,
                    movies = emptyList(),
                    isLoading = true
                ) }

                loadMovies(language = appLocale, forceRefresh = isNewLanguage)
            }
        }
    }

    fun mapLocale(locale: String) = when (locale) {
        "in" -> "id"
        else -> "en-US"
    }

    private fun loadMovies(
        language: String = _uiState.value.selectedLanguage,
        forceRefresh: Boolean = false
    ) {
        fetchJob?.cancel()

        fetchJob = viewModelScope.launch {
            repository.getPopularMovies(language, forceRefresh).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = ErrorMessage.None) }
                    }
                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(
                            movies = response.data,
                            isLoading = false,
                            errorMessage = ErrorMessage.None
                        ) }
                    }
                    is ApiResponse.Error -> {
                        val message = when (response.errorType) {
                            ErrorType.NO_INTERNET -> R.string.error_no_internet
                            ErrorType.NO_RESULTS -> R.string.error_no_movies
                            ErrorType.SERVER_OR_CLIENT_ERROR -> R.string.error_server_client
                            ErrorType.TIMEOUT -> R.string.error_timeout
                            else -> R.string.error_unexpected
                        }
                        _uiState.update { it.copy(
                            errorMessage = ErrorMessage.ErrorMessageRes(message),
                            isLoading = false
                        ) }
                    }
                }
            }
        }
    }

    fun refresh() {
        loadMovies(forceRefresh = true)
    }

    fun updateCurrentItem(index: Int) {
        val selectedItem = _uiState.value.movies.getOrNull(index)
        Timber.d("Selected at index $index of movieTitle: ${selectedItem?.title}")

        _uiState.value = _uiState.value.copy(
            currentItemIndex = index
        )
    }

    fun updateLocale(locale: String) {
        _uiState.value = _uiState.value.copy(
            selectedLocale = locale
        )
        val appLocale = LocaleListCompat.forLanguageTags(locale)
        AppCompatDelegate.setApplicationLocales(appLocale)

        viewModelScope.launch {
            preferencesRepository.saveLocale(locale)
        }
    }

    fun onIntentButtonClicked() {
        Timber.d("Intent button clicked")
    }

    companion object {
        fun Factory(
            initialLocale: String,
            repository: IMovieRepository,
            preferencesRepository: AppPreferencesRepository
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ScrollableViewModel(initialLocale, repository, preferencesRepository)
            }
        }
    }
}