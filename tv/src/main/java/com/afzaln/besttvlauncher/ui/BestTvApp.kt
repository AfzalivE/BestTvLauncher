package com.afzaln.besttvlauncher.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.FadeTransition
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.ui.apps.AppsScreen
import com.afzaln.besttvlauncher.ui.channels.ChannelsScreen
import com.afzaln.besttvlauncher.ui.home.HomeScreen
import com.afzaln.besttvlauncher.ui.theme.AppTheme

@Composable
fun BestTvApp() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Navigator(screen = Home)
        }
    }
}

object Home : Screen {
    @Composable
    override fun Content() = HomeScreen()
}

object Channels : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(id = R.string.channels)
            return remember {
                TabOptions(0u, title)
            }
        }

    @Composable
    override fun Content() = ChannelsScreen()
}

object Apps : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(id = R.string.your_apps)
            return remember {
                TabOptions(1u, title)
            }
        }

    @Composable
    override fun Content() = AppsScreen()
}

class ItemDetails(private val channelId: Long, private val programId: Long) : Screen {
    @Composable
    override fun Content() = ItemDetailsScreen(channelId, programId)
}
