package com.example.scrollablemodul3

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollablemodul3.ui.screen.DetailScreen
import com.example.scrollablemodul3.ui.screen.HomeScreen
import com.example.scrollablemodul3.ui.ScrollableViewModel
import com.example.scrollablemodul3.ui.screen.SettingScreen

enum class ScrollableScreen { Home, Details, Settings }

@Preview(showBackground = true)
@Composable
fun ScrollableAppPreview() {
    ScrollableApp()
}
@Composable
fun ScrollableApp(
    viewModel: ScrollableViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    NavHost(
            navController = navController,
            startDestination = ScrollableScreen.Home.name
    ) {
        composable(route = ScrollableScreen.Home.name) {
            val activity = context as Activity
            HomeScreen(
                uiState = uiState,
                onDetailClick = { index ->
                    navController.navigate("${ScrollableScreen.Details.name}/$index")
                },
                onIntentClick = { url ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                },
                onSettingsClick = { navController.navigate(ScrollableScreen.Settings.name) },
                onExit = { activity.finish() }
                )
        }
        composable(route = "${ScrollableScreen.Details.name}/{itemId}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
            val item = uiState.list.getOrNull(index) ?: uiState.list.first()
            LaunchedEffect(index){
                viewModel.updateCurrentScrollable(index)
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
    }
}