package com.example.scrollablemodul3

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollablemodul3.ui.gamelist.screen.DetailScreen
import com.example.scrollablemodul3.ui.gamelist.screen.HomeScreen
import com.example.scrollablemodul3.ui.gamelist.ScrollableViewModel
import com.example.scrollablemodul3.ui.gamelist.screen.SettingScreen
import com.example.scrollablemodul3.ui.movie.screen.MovieScreen

enum class ScrollableScreen { Home, Details, Settings, Movie }

@Composable
fun ScrollableApp(
    navController: NavHostController = rememberNavController()
) {
    val defaultLocale = AppCompatDelegate.getApplicationLocales().get(0)?.language ?: "en"
    val app = LocalContext.current.applicationContext as ScrollableApplication
    val viewModel: ScrollableViewModel = viewModel(
        factory = ScrollableViewModel.Factory(
            initialLocale = defaultLocale,
            preferencesRepository = app.preferencesRepository
        )
    )
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = ScrollableScreen.Home.name,
    ) {
        composable(route = ScrollableScreen.Home.name) {
            val activity = context as Activity
            HomeScreen(
                uiState = uiState,
                onDetailClick = { index ->
                    navController.navigate("${ScrollableScreen.Details.name}/$index")
                    viewModel.onDetailButtonClicked()
                },
                onIntentClick = { url ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                    viewModel.onIntentButtonClicked()
                },
                onSettingsClick = { navController.navigate(ScrollableScreen.Settings.name) },
                onMovieClick = { navController.navigate(ScrollableScreen.Movie.name) },
                onExit = { activity.finish() }
                )
        }
        composable(route = "${ScrollableScreen.Details.name}/{itemId}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
            val item = uiState.list.getOrNull(index) ?: uiState.list.first()
            LaunchedEffect(index){
                viewModel.updateCurrentItem(index)
            }
            DetailScreen(
                item = item,
                modifier = Modifier,
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(route = ScrollableScreen.Settings.name) {
            SettingScreen(
                modifier = Modifier,
                onBackClick = { navController.navigateUp() },
                onLocaleChange = { locale -> viewModel.updateLocale(locale) },
                selectedLocale = uiState.selectedLocale
            )
        }
        composable(route = ScrollableScreen.Movie.name) {
            MovieScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}