package com.example.scrollablemodul3

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scrollablemodul3.ui.DetailScreen
import com.example.scrollablemodul3.ui.HomeScreen
import com.example.scrollablemodul3.ui.ScrollableViewModel
import com.example.scrollablemodul3.ui.SettingScreen

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
            HomeScreen(
                uiState = uiState,
                onDetailClick = { index ->
                    navController.navigate("${ScrollableScreen.Details.name}/$index")
                },
                onIntentClick = { url ->
                    context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                },
                modifier = Modifier
                )
        }
        composable(route = "${ScrollableScreen.Details.name}/{itemId}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
            LaunchedEffect(index){
                viewModel.updateCurrentScrollable(index)
            }
            DetailScreen(
                item = uiState.list[index],
                modifier = Modifier,
                onBackClick = { navController.navigateUp() }
            )
        }
        composable(route = ScrollableScreen.Settings.name) {
            SettingScreen(
                modifier = Modifier,
                onBackClick = { navController.navigateUp() },
                onLocaleChange = { locale -> viewModel.updateLocale(locale) }
            )
        }
    }
}