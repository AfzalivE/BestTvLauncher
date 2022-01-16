package com.afzaln.besttvlauncher.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.afzaln.besttvlauncher.ui.apps.AppsScreen
import com.afzaln.besttvlauncher.ui.channels.ChannelsScreen
import com.afzaln.besttvlauncher.ui.home.HomeScaffold
import com.afzaln.besttvlauncher.ui.theme.AppTheme

@Composable
fun BestTvApp() {
    AppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScaffold(
                pages = listOf(
                    { ChannelsScreen() },
                    { AppsScreen() }
                )
            )
        }
    }
}
