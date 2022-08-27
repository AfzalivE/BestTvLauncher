package com.afzaln.besttvlauncher.ui.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import com.afzaln.besttvlauncher.ui.Apps
import com.afzaln.besttvlauncher.ui.Channels
import com.afzaln.besttvlauncher.ui.ItemDetails
import com.afzaln.besttvlauncher.ui.apps.AppsScreen
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.channels.ChannelsScreen
import com.afzaln.besttvlauncher.ui.components.TitleBar
import com.afzaln.besttvlauncher.ui.itemdetails.ItemDetailsScreen
import com.afzaln.besttvlauncher.utils.locatorViewModel
import com.afzaln.besttvlauncher.utils.navigateSingleTopTo
import com.afzaln.besttvlauncher.utils.navigateToItemDetails
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun HomeScreen() {
    val tabs = listOf(Channels, Apps)
    val viewModel: HomeViewModel = locatorViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(HomeViewModel.State.Loading)

    val navController = rememberAnimatedNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentTab = tabs.find { it.route == currentDestination?.route } ?: Channels

    LaunchedEffect(key1 = Unit, block = {
        viewModel.loadData()
    })

    Column(Modifier.fillMaxSize()) {
        TitleBar(
            modifier = Modifier.padding(top = 34.dp),
            selectedTab = currentTab,
            tabs = tabs,
            onTabSelected = { navController.navigateSingleTopTo(it.route) }
        )

        AnimatedNavHost(
            navController = navController,
            startDestination = Channels.route,
        ) {
            composable(
                route = Channels.route,
                enterTransition = {
                    tabEnterTransition(AnimatedContentScope.SlideDirection.End)
                },
                exitTransition = {
                    tabExitTransition(AnimatedContentScope.SlideDirection.Start)
                }
            ) {
                ChannelsScreen(
                    state = state,
                    onProgramClicked = { channelId, programId ->
                        navController.navigateToItemDetails(channelId, programId)
                    }
                )
            }

            composable(route = Apps.route,
                enterTransition = {
                    tabEnterTransition(AnimatedContentScope.SlideDirection.Start)
                },
                exitTransition = {
                    tabExitTransition(AnimatedContentScope.SlideDirection.End)
                }
            ) {
                AppsScreen(state)
            }

            itemDetailsComposable()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.tabExitTransition(
    slideDirection: AnimatedContentScope.SlideDirection,
    duration: Int = 500
) = fadeOut(tween(duration / 2, easing = LinearEasing)) + slideOutOfContainer(
    slideDirection,
    tween(duration, easing = LinearEasing),
    targetOffset = { it / 24 }
)

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentScope<NavBackStackEntry>.tabEnterTransition(
    slideDirection: AnimatedContentScope.SlideDirection,
    duration: Int = 500,
    delay: Int = duration - 350
) = fadeIn(tween(duration, duration - delay)) + slideIntoContainer(
    slideDirection,
    animationSpec = tween(duration, duration - delay),
    initialOffset = { it / 24 }
)

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.itemDetailsComposable() {
    composable(
        route = ItemDetails.routeWithArgs,
        arguments = ItemDetails.arguments
    ) { navBackstackEntry ->
        navBackstackEntry.arguments?.let { arguments ->
            val channelId = arguments.getLong(ItemDetails.channelIdArg)
            val programId = arguments.getLong(ItemDetails.programIdArg)
            ItemDetailsScreen(channelId = channelId, programId = programId)
        }
    }
}
