package com.afzaln.besttvlauncher.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.ScreenTransition
import com.afzaln.besttvlauncher.ui.Apps
import com.afzaln.besttvlauncher.ui.Channels

@Composable
fun HomeScreen() {
    val tabs = listOf(Channels, Apps)

    TabNavigator(tab = Channels) {
        val tabNavigator = LocalTabNavigator.current
        val navigator = LocalNavigator.currentOrThrow

        HomeScaffold(
            selectedTab = tabNavigator.current,
            tabs = tabs,
            onTabSelected = {
                tabNavigator.current = it
            }
        ) {
            TabTransition(navigator)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TabTransition(navigator: Navigator) {
    ScreenTransition(navigator = navigator, transition = {
        if (Channels isTransitioningTo Apps) {
            fadeIn(tween(duration)) +
                    slideInHorizontally(tween(duration), initialOffsetX = { xOffset }) with
                    fadeOut(tween(duration)) +
                    slideOutHorizontally(tween(duration), targetOffsetX = { -xOffset })
        } else if (Apps isTransitioningTo Channels) {
            fadeIn(tween(duration)) +
                    slideInHorizontally(tween(duration), initialOffsetX = { -xOffset }) with
                    fadeOut(tween(duration)) +
                    slideOutHorizontally(
                        tween(duration),
                        targetOffsetX = { xOffset }
                    )
        } else {
            fadeIn() with fadeOut()
        }
    })
}

const val duration = 1000
const val xOffset = 1000
