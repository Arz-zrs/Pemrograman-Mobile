package com.example.scrollablexml.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.example.scrollablexml.model.DataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScrollableViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ScrollableUiState())
    val uiState: StateFlow<ScrollableUiState> = _uiState.asStateFlow()

    init {
        val items = DataSource().loadItems()
        val currentLocale = AppCompatDelegate.getApplicationLocales().get(0)?.language ?: "en"
        _uiState.value = ScrollableUiState(
            list = items,
            currentItemIndex = 0,
            selectedLocale = currentLocale
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