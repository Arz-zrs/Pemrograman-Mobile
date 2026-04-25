package com.example.scrollablemodul3

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.scrollablemodul3.ui.ScrollableViewModel

enum class ScrollableAppScreen {
    Home,
    Details,
    Settings
}

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
    Text(
        text = "Hello!"
    )
}

