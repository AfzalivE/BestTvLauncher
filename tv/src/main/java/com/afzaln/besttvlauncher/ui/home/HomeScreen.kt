package com.afzaln.besttvlauncher.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.afzaln.besttvlauncher.ui.apps.AppsScreen
import com.afzaln.besttvlauncher.ui.channels.ChannelsScreen
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalAnimationApi::class)
@Destination(start = true)
@Composable
fun HomeScreen(navController: NavHostController) {

    var selectedTab by remember { mutableStateOf(CHANNEL_SCREEN) }

    HomeScaffold(
        selectedTabIndex = selectedTab,
        onTabSelected = {
            selectedTab = it
        }
    ) {
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides left and fades in
                    // while the initial (smaller) number slides down and fades out.
                    slideInHorizontally(animationSpec = tween(3000)) { width -> width } + fadeIn() with
                            slideOutHorizontally { width -> -width } + fadeOut(tween(3000))
                } else {
                    // If the target number is smaller, it slides right and fades in
                    // while the initial number left and fades out.
                    slideInHorizontally { width -> -width } + fadeIn() with
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            }
        ) { targetCount ->
            when (targetCount) {
                CHANNEL_SCREEN -> ChannelsScreen(navController)
                APPS_SCREEN -> AppsScreen()
            }
        }
    }
}

const val CHANNEL_SCREEN = 0
const val APPS_SCREEN = 1
