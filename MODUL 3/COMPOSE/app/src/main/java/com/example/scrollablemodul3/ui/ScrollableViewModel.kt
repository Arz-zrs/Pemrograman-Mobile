package com.example.scrollablemodul3.ui

import androidx.lifecycle.ViewModel
import com.example.scrollablemodul3.model.DataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScrollableViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ScrollableUiState())
    val uiState: StateFlow<ScrollableUiState> = _uiState.asStateFlow()

    init {
        loadScrollable()
    }

    fun loadScrollable(){
        val items = DataSource().loadItems()
        _uiState.value = ScrollableUiState(
            list = items,
            currentScrollable = items.first(),
            currentScrollableIndex = 0
        )
    }

    fun updateCurrentScrollable(index: Int) {
        val items = _uiState.value.list
        _uiState.value = ScrollableUiState(
            list = items,
            currentScrollable = items[index],
            currentScrollableIndex = index
        )
    }
}