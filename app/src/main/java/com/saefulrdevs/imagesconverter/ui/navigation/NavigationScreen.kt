package com.saefulrdevs.imagesconverter.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.saefulrdevs.imagesconverter.R
import com.saefulrdevs.imagesconverter.ui.screens.documentscannerscreen.DocumentScannerScreen
import com.saefulrdevs.imagesconverter.ui.screens.homescreen.HomeScreen
import com.saefulrdevs.imagesconverter.ui.screens.textrecognitionscreen.CameraRecognitionScreen

enum class Screen(@StringRes val title: Int) {
    CameraRecognitionScreen(R.string.ImagePickerScreen),
    DocumentScannerScreen(R.string.imageScannerScreen),
    HomeScreen(R.string.HomeScreen)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title), onTextLayout = {}) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavigationScreen(navController: NavHostController = rememberNavController()) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.HomeScreen.name
    )

    Scaffold(
        topBar = {
            AppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HomeScreen.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = Screen.HomeScreen.name) {
                HomeScreen(
                    navigateToCameraRecognitionScreen = { navController.navigate(Screen.CameraRecognitionScreen.name) },
                    navigateToDocumentScannerScreen = { navController.navigate(Screen.DocumentScannerScreen.name) },
                )
            }
            composable(route = Screen.DocumentScannerScreen.name) {
                DocumentScannerScreen()
            }
            composable(route = Screen.CameraRecognitionScreen.name) {
                CameraRecognitionScreen()
            }
        }
    }
}