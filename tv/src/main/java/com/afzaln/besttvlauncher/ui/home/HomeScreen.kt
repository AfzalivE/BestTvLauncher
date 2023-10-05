package com.afzaln.besttvlauncher.ui.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import com.afzaln.besttvlauncher.ui.Apps
import com.afzaln.besttvlauncher.ui.Channels
import com.afzaln.besttvlauncher.ui.ItemDetails
import com.afzaln.besttvlauncher.ui.apps.AppsScreen
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.channels.ChannelsScreen
import com.afzaln.besttvlauncher.ui.components.TitleBar
import com.afzaln.besttvlauncher.ui.itemdetails.ItemDetailsScreen
import com.afzaln.besttvlauncher.utils.emptyPalette
import com.afzaln.besttvlauncher.utils.locatorViewModel
import com.afzaln.besttvlauncher.utils.navigateSingleTopTo
import com.afzaln.besttvlauncher.utils.navigateToItemDetails

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen() {
    val tabs = listOf(Channels, Apps)
    val viewModel: HomeViewModel = locatorViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle(HomeViewModel.State.Loading)

    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentTab = tabs.find { it.route == currentDestination?.route } ?: Channels

    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
    }

    var palette by remember { mutableStateOf(emptyPalette()) }

    Column(
        Modifier
            .fillMaxSize()
            .animatedBackground(
                palette.vibrantSwatch,
                MaterialTheme.colorScheme.background
            )
    ) {
        TitleBar(
            modifier = Modifier
                .padding(top = 34.dp)
                .onFocusChanged { focusState ->
                    if (focusState.hasFocus) {
                        palette = emptyPalette()
                    }
                },
            selectedTab = currentTab,
            tabs = tabs,
            onTabSelected = { navController.navigateSingleTopTo(it.route) }
        )

        NavHost(
            navController = navController,
            startDestination = Channels.route,
        ) {
            composable(
                route = Channels.route,
                enterTransition = {
                    tabEnterTransition(AnimatedContentTransitionScope.SlideDirection.End)
                },
                exitTransition = {
                    tabExitTransition(AnimatedContentTransitionScope.SlideDirection.Start)
                }
            ) {
                ChannelsScreen(
                    state = state,
                    onProgramClicked = { channelId, programId ->
                        navController.navigateToItemDetails(channelId, programId)
                    }
                ) { newPalette ->
                    palette = newPalette
                }
            }

            composable(route = Apps.route,
                enterTransition = {
                    tabEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start)
                },
                exitTransition = {
                    tabExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
                }
            ) {
                AppsScreen(state)
            }

            itemDetailsComposable()
        }
    }
}

private fun Modifier.animatedBackground(
    swatch: Palette.Swatch?,
    fallbackColor: Color
): Modifier = composed {
    val backgroundColor by remember(swatch, fallbackColor) {
        derivedStateOf {
            swatch?.rgb?.let { vibrantRgb ->
                Color(vibrantRgb)
            } ?: fallbackColor
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor.copy(0.10f),
        animationSpec = tween(500)
    )

    then(background(animatedBackgroundColor))
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.tabExitTransition(
    slideDirection: AnimatedContentTransitionScope.SlideDirection,
    duration: Int = 500
) = fadeOut(tween(duration / 2, easing = LinearEasing)) + slideOutOfContainer(
    slideDirection,
    tween(duration, easing = LinearEasing),
    targetOffset = { it / 24 }
)

private fun AnimatedContentTransitionScope<NavBackStackEntry>.tabEnterTransition(
    slideDirection: AnimatedContentTransitionScope.SlideDirection,
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

@Preview
@Composable
fun AnimatedBackgroundPreview() {
    var palette by remember { mutableStateOf(emptyPalette()) }
    val materialBackground = MaterialTheme.colorScheme.background
    val backgroundColor by remember(palette, materialBackground) {
        derivedStateOf {
            palette.vibrantSwatch?.rgb?.let { vibrantRgb ->
                Color(vibrantRgb).copy(0.05f)
            } ?: materialBackground
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(20050)
    )

    Surface(
        Modifier
            .requiredWidth(240.dp)
            .requiredHeight(240.dp)
            .background(animatedBackgroundColor)
    ) {
//        Button()
    }
}
