package com.example.scrollablemodul3.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrollablemodul3.R
import com.example.scrollablemodul3.model.ScrollableData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    item: ScrollableData,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = item.titleResourceId))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = item.imageResourceId),
                contentDescription = stringResource(id = item.titleResourceId),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = item.titleResourceId),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(id = item.subtitleResourceId),
                    style = MaterialTheme.typography.headlineSmall
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = stringResource(id = item.descriptionResourceId),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(id = item.detailResourceId),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        item = ScrollableData(
            id = 1,
            titleResourceId = R.string.item5,
            subtitleResourceId = R.string.item5_sub,
            descriptionResourceId = R.string.item5_desc,
            detailResourceId = R.string.item5_detail,
            imageResourceId = R.drawable.sekiro,
            steamUrl = "https://store.steampowered.com/app/1091500/Sekiro_Shadows_Die_Twice/"
        ),
        modifier = Modifier,
        onBackClick = {}
    )
}