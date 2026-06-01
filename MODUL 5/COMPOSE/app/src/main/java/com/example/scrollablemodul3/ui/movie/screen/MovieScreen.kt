package com.example.scrollablemodul3.ui.movie.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.example.scrollablemodul3.R
import com.example.scrollablemodul3.ScrollableApplication
import com.example.scrollablemodul3.model.data.local.entity.MovieEntity
import com.example.scrollablemodul3.model.data.repository.MovieRepository
import com.example.scrollablemodul3.ui.movie.ErrorMessage
import com.example.scrollablemodul3.ui.movie.MovieUiState
import com.example.scrollablemodul3.ui.movie.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val app = context.applicationContext as ScrollableApplication
    val repository = MovieRepository(movieDao = app.database.movieDao())
    val viewModel: MovieViewModel = viewModel(
        factory = MovieViewModel.Factory(repository, app.preferencesRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.movie_list_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh_button)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            MovieCategoryChips(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it, forceRefresh = true) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onRefresh = { viewModel.refresh() }
            )

            if (uiState.movies.isEmpty() && uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            MovieContent(
                uiState = uiState,
                onRefresh = { viewModel.refresh() }
            )
        }
    }
}

@Composable
fun MovieContent(
    uiState: MovieUiState,
    onRefresh: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.movies.isEmpty() && uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.errorMessage is ErrorMessage.ErrorMessageRes && uiState.movies.isEmpty() -> {
                val error = uiState.errorMessage
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                ) {
                    Text(
                        text = stringResource(error.messageRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(onClick = onRefresh) {
                        Text(text = stringResource(R.string.refresh_button))
                    }
                }
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.movies, key = { it.id }) { index ->
                        MovieCard(movie = index)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: MovieEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        if (!movie.posterPath.isNullOrEmpty()) {
                            "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                        } else {
                            R.drawable.image_error
                        }
                    )
                    .crossfade(true)
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_error)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(90.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier.width(16.dp))
            Column {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = stringResource(R.string.rating_format),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.rating_format, movie.voteAverage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.votes_format, movie.voteCount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = movie.releaseDate.ifEmpty { stringResource(R.string.unknown) },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier.height(8.dp))
                Text(
                    text = movie.overview.ifEmpty { stringResource(R.string.no_overview) },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun MovieCategoryChips(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "popular" to stringResource(R.string.category_popular),
        "now_playing" to stringResource(R.string.category_now_playing),
        "top_rated" to stringResource(R.string.category_top_rated)
    )
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { (category, label) ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = {
                    onCategorySelected(category)
                    onRefresh()
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun MovieScreenPreview() {
    MovieCategoryChips(
        selectedCategory = "popular",
        onCategorySelected = {},
        onRefresh = {}
    )
}