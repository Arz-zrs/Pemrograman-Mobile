package com.example.scrollablemodul3.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.example.scrollablemodul3.model.DataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScrollableViewModel(initialLocale: String) : ViewModel() {
    private val _uiState = MutableStateFlow(ScrollableUiState(selectedLocale = initialLocale))
    val uiState: StateFlow<ScrollableUiState> = _uiState.asStateFlow()

    init {
        val items = DataSource().loadItems()
        _uiState.value = ScrollableUiState(
            list = items,
            currentItemIndex = 0,
            selectedLocale = initialLocale
        )
    }

    fun updateCurrentItem(index: Int) {
        _uiState.value = _uiState.value.copy(
            currentItemIndex = index
        )
    }

    fun updateLocale(locale: String) {
        _uiState.value = _uiState.value.copy(
            selectedLocale = locale,
        )
        val appLocale = LocaleListCompat.forLanguageTags(locale)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}