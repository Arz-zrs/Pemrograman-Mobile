package com.example.scrollablemodul3.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollablemodul3.model.ScrollableData

@Composable
fun HomeScreen(
    uiState: ScrollableUiState,
    onDetailClick: (Int) -> Unit,
    onIntentClick: (String) -> Unit,
    modifier: Modifier
){
    LazyColumn(modifier = modifier) {
        item {
            ItemCarousel(
                items = uiState.list
            )
        }
        itemsIndexed(uiState.list) { index, item ->
            ItemCard(
                item = item,
                onDetailClick = { onDetailClick(index) },
                onIntentClick = { onIntentClick(item.steamUrl) }
            )
        }
    }
}

@Composable
fun ItemCard(
    item: ScrollableData,
    onDetailClick: () -> Unit,
    onIntentClick: () -> Unit
) {
    TODO("Not yet implemented")
}

@Composable
fun ItemCarousel(items: List<ScrollableData>) {
    TODO("Not yet implemented")
}