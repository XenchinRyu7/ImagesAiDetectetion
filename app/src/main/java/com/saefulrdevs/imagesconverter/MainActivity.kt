package com.saefulrdevs.imagesconverter

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.saefulrdevs.imagesconverter.ui.navigation.NavigationScreen
import com.saefulrdevs.imagesconverter.ui.theme.ImagesConverterTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            ImagesConverterTheme {
                NavigationScreen()
            }
        }
    }
}
