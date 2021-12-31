package com.afzaln.besttvlauncher.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.afzaln.besttvlauncher.ui.home.HomeScreen
import com.afzaln.besttvlauncher.ui.theme.BestTvLauncherTheme

@Composable
fun BestTvApp() {
    BestTvLauncherTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
}
