package com.example.scrollablemodul3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrollablemodul3.R
import com.example.scrollablemodul3.ScrollableScreen
import com.example.scrollablemodul3.model.ScrollableData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    currentScreen: ScrollableScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onLocaleChange: (String) -> Unit,
    onExit: () -> Unit
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.head_title))
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
            else {
                IconButton(onClick = onExit) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = stringResource(R.string.exit_button)
                    )
                }
            }
        },
        actions = {
            if (currentScreen == ScrollableScreen.Home) {
                IconButton(onClick = { onLocaleChange("en") }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.setting_button)
                    )
                }
            }
        }
    )
}
@Composable
fun HomeScreen(
    uiState: ScrollableUiState,
    onDetailClick: (Int) -> Unit,
    onIntentClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            HomeAppBar(
                currentScreen = ScrollableScreen.Home,
                canNavigateBack = false,
                navigateUp = {},
                onLocaleChange = {},
                onExit = {}
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = modifier.padding(innerPadding)) {
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
}

@Composable
fun ItemCard(
    item: ScrollableData,
    onDetailClick: () -> Unit,
    onIntentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = item.imageResourceId),
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(modifier = modifier) {
                Text(text = stringResource(id = item.titleResourceId))
                Text(text = stringResource(id = item.subtitleResourceId))

                Row(modifier = modifier
                    .padding(top = 24.dp)
                ) {
                    Button(onClick = onIntentClick) {
                        Text(stringResource(R.string.steam_button))
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(onClick = onDetailClick) {
                        Text(stringResource(R.string.detail_button))
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCarousel(
    items: List<ScrollableData>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(listState)
    ) {
        itemsIndexed(items) { _, item ->
            ItemCard(
                item = item,
                onDetailClick = {},
                onIntentClick = {}
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    ItemCarousel(
        items = listOf(
            ScrollableData(
                id = 1,
                titleResourceId = R.string.item1,
                subtitleResourceId = R.string.item1_sub,
                descriptionResourceId = R.string.item1_desc,
                detailResourceId = R.string.item1_detail,
                imageResourceId = R.drawable.crosscode,
                steamUrl = "https://store.steampowered.com/app/368340/CrossCode/"
            ),
        ),
        modifier = Modifier
    )
}