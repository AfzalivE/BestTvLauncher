package com.afzaln.besttvlauncher.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.afzaln.besttvlauncher.ui.apps.AppsScreen
import com.afzaln.besttvlauncher.ui.channels.ChannelsScreen
import com.afzaln.besttvlauncher.ui.destinations.HomeScreenDestination
import com.afzaln.besttvlauncher.ui.destinations.ItemDetailsScreenDestination
import com.afzaln.besttvlauncher.ui.home.HomeScaffold
import com.afzaln.besttvlauncher.ui.home.HomeScreen
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.utils.composable

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
