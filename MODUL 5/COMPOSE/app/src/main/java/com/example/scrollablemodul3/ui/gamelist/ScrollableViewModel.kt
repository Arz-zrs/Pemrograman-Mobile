package com.example.scrollablemodul3.ui.gamelist

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scrollablemodul3.model.DataSource
import com.example.scrollablemodul3.model.data.datastore.AppPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ScrollableViewModel(
    initialLocale: String,
    private val preferencesRepository: AppPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScrollableUiState(selectedLocale = initialLocale))
    val uiState: StateFlow<ScrollableUiState> = _uiState.asStateFlow()

    init {
        val items = DataSource().loadItems()
        Timber.d("Items loaded: ${items.size}")
        Timber.d("Items: $items")
        _uiState.value = ScrollableUiState(
            list = items,
            currentItemIndex = 0,
            selectedLocale = initialLocale
        )

        viewModelScope.launch {
            preferencesRepository.selectedLocale.collect { savedLocale ->
                if (_uiState.value.selectedLocale != savedLocale) {
                    _uiState.value = _uiState.value.copy(selectedLocale = savedLocale)
                }
            }
        }
    }

    fun updateCurrentItem(index: Int) {
        val selectedItem = _uiState.value.list.getOrNull(index)
        Timber.d("Selected at index $index of itemId: ${selectedItem?.id}")
        Timber.d("Selected items: $selectedItem")

        _uiState.value = _uiState.value.copy(
            currentItemIndex = index
        )
    }

    fun onDetailButtonClicked() {
        Timber.d("Detail button clicked")
    }

    fun onIntentButtonClicked(){
        Timber.d("Intent button clicked")
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

    companion object {
        fun Factory(
            initialLocale: String,
            preferencesRepository: AppPreferencesRepository
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ScrollableViewModel(initialLocale, preferencesRepository)
            }
        }
    }
}