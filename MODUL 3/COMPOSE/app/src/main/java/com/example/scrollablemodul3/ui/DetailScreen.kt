package com.example.scrollablemodul3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.scrollablemodul3.model.ScrollableData

@Composable
fun DetailScreen(
    item: ScrollableData,
    modifier: Modifier
){
    Column(modifier = modifier) {
        Image(
            painter = painterResource(item.imageResourceId),
            contentDescription = stringResource(item.titleResourceId),
            modifier = Modifier
        )
        Text(text = stringResource(item.titleResourceId))
        Text(text = stringResource(item.subtitleResourceId))
        Text(text = stringResource(item.descriptionResourceId))
        Text(text = stringResource(item.detailResourceId))
    }
}