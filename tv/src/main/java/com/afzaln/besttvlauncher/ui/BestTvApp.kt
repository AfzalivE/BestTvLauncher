package com.afzaln.besttvlauncher.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.afzaln.besttvlauncher.ui.apps.AppsScreen
import com.afzaln.besttvlauncher.ui.channels.ChannelsScreen
import com.afzaln.besttvlauncher.ui.home.HomeScaffold
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BestTvApp() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            val parentNavController = rememberAnimatedNavController()
            val navController = rememberAnimatedNavController()
            var selectedTabIndex by remember { mutableStateOf(0) }
            val tabs = listOf(
                "channels",
                "apps"
            )
            AnimatedNavHost(navController = parentNavController, startDestination = "home") {
                composable("home") {
                    HomeScaffold(
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { index ->
                            selectedTabIndex = index
                            navController.navigate(tabs[index])
                        }
                    ) {
                        AnimatedNavHost(
                            navController = navController,
                            startDestination = "channels"
                        ) {
                            composable("channels") {
                                ChannelsScreen(navController = parentNavController)
                            }
                            composable("apps") {
                                AppsScreen()
                            }
                        }
                    }
                }
                composable(
                    route = "itemdetails/{channelId}/{programId}",
                    arguments = listOf(
                        navArgument("channelId") {
                            type = NavType.LongType
                        },
                        navArgument("programId") {
                            type = NavType.LongType
                        }
                    )
                ) { backstackEntry ->
                    ItemDetailsScreen(
                        channelId = backstackEntry.arguments?.getLong("channelId")
                            ?: -1,
                        programId = backstackEntry.arguments?.getLong("programId") ?: -1
                    )
                }
            }
        }
    }
}
