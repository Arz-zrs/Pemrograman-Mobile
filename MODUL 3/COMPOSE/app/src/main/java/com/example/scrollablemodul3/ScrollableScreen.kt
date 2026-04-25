package com.example.scrollablemodul3

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ScrollableScreen.valueOf(
        backStackEntry?.destination?.route ?: ScrollableScreen.Home.name
    )

    Scaffold(
        topBar = {
            // TODO: Create Composable Header with exit and language Icon options
            ScrollableAppBar(
                currentScreen = ScrollableScreen.Home,
                canNavigateBack = false,
                navigateUp = { /*TODO*/ },
                onLocaleChange = { locale -> viewModel.updateLocale(locale) },
                onExit = { /*TODO*/ }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = ScrollableScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ScrollableScreen.Home.name) {
                HomeScreen(
                    uiState = uiState,
                    onDetailClick = { index ->
                        navController.navigate("${ScrollableScreen.Details.name}/$index")
                    },
                    onIntentClick = { url -> /*TODO*/ },
                    modifier = Modifier
                )
            }
            composable(route = "${ScrollableScreen.Details.name}/{itemId}") { backStackEntry ->
                val index = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0
                viewModel.updateCurrentScrollable(index)
                DetailScreen(
                    item = uiState.list[index],
                    modifier = Modifier
                )
            }
            composable(route = ScrollableScreen.Settings.name) {
                SettingScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableAppBar(
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