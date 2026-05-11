package com.example.scrollablemodul3.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScrollableViewModelFactory(private val defaultLocale: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScrollableViewModel::class.java)) {
            return ScrollableViewModel(defaultLocale) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}