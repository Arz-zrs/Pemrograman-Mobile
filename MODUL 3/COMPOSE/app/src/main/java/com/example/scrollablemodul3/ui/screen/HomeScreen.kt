package com.example.scrollablemodul3.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrollablemodul3.R
import com.example.scrollablemodul3.model.ScrollableData
import com.example.scrollablemodul3.ui.ScrollableUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    onSettingClick: () -> Unit,
    onExit: () -> Unit
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.head_title))
        },
        navigationIcon = {
            IconButton(onClick = onExit) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        actions = {
            IconButton(onClick =  onSettingClick ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.setting_button)
                )
            }
        }
    )
}
@Composable
fun HomeScreen(
    uiState: ScrollableUiState,
    onDetailClick: (Int) -> Unit,
    onIntentClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            HomeAppBar(
                onSettingClick = onSettingsClick,
                onExit = onExit
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = modifier
            .padding(innerPadding)
            .padding(start = 8.dp)
        ) {
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
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = item.imageResourceId),
                contentDescription = stringResource(id = item.titleResourceId),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = item.titleResourceId),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(id = item.subtitleResourceId),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = item.descriptionResourceId),
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = onIntentClick) {
                        Text(
                            text = stringResource(R.string.steam_button),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(
                        onClick = onDetailClick
                    ) {
                        Text(
                            text = stringResource(R.string.detail_button),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CarouselCard(
    item: ScrollableData,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = item.imageResourceId),
                contentDescription = stringResource(id = item.titleResourceId),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = stringResource(id = item.titleResourceId),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(id = item.subtitleResourceId),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = item.descriptionResourceId),
                    style = MaterialTheme.typography.bodyMedium
                )}
        }
    }

}

@Composable
fun ItemCarousel(
    items: List<ScrollableData>,
) {
    val listState = rememberLazyListState()
    LazyRow(
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(listState)
    ) {
        itemsIndexed(items) { _, item ->
            CarouselCard(
                item = item,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}

@Preview(device = "id:pixel_5")
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = ScrollableUiState(
            list = listOf(
                ScrollableData(
                    id = 1,
                    titleResourceId = R.string.item1,
                    subtitleResourceId = R.string.item1_sub,
                    descriptionResourceId = R.string.item1_desc,
                    detailResourceId = R.string.item1_detail,
                    imageResourceId = R.drawable.crosscode,
                    steamUrl = "https://store.steampowered.com/app/368340/CrossCode/"
                ),
                ScrollableData(
                    id = 2,
                    titleResourceId = R.string.item2,
                    subtitleResourceId = R.string.item2_sub,
                    descriptionResourceId = R.string.item2_desc,
                    detailResourceId = R.string.item2_detail,
                    imageResourceId = R.drawable.hades2,
                    steamUrl = "https://store.steampowered.com/app/368340/CrossCode/"
                ),
                ScrollableData(
                    id = 3,
                    titleResourceId = R.string.item3,
                    subtitleResourceId = R.string.item3_sub,
                    descriptionResourceId = R.string.item3_desc,
                    detailResourceId = R.string.item3_detail,
                    imageResourceId = R.drawable.nms,
                    steamUrl = "https://store.steampowered.com/app/368340/CrossCode/"
                ),
                ScrollableData(
                    id = 4,
                    titleResourceId = R.string.item4,
                    subtitleResourceId = R.string.item4_sub,
                    descriptionResourceId = R.string.item4_desc,
                    detailResourceId = R.string.item4_detail,
                    imageResourceId = R.drawable.coe33,
                    steamUrl = "https://store.steampowered.com/app/368340/CrossCode/"
                ),
                ScrollableData(
                    id = 5,
                    titleResourceId = R.string.item5,
                    subtitleResourceId = R.string.item5_sub,
                    descriptionResourceId = R.string.item5_desc,
                    detailResourceId = R.string.item5_detail,
                    imageResourceId = R.drawable.sekiro,
                    steamUrl = "https://store.steampowered.com/app/368340/CrossCode/"
                ),
            )
        ),

        onDetailClick = {},
        onIntentClick = {},
        onSettingsClick = {},
        onExit = {}
    )
}