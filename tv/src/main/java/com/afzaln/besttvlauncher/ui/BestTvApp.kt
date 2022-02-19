package com.afzaln.besttvlauncher.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.ramcosta.composedestinations.DestinationsNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BestTvApp() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DestinationsNavHost(navGraph = NavGraphs.root)
        }
    }
}
