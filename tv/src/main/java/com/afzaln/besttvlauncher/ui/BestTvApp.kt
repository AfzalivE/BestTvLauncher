package com.afzaln.besttvlauncher.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.ui.home.HomeScreen
import com.afzaln.besttvlauncher.ui.theme.AppTheme

@Composable
fun BestTvApp() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
}

interface Destination {
    val route: String
}

interface Tab: Destination {
    @get:Composable
    val title: String
}

object Channels : Tab {
    override val route = "channels"

    override val title: String
        @Composable
        get() = stringResource(id = R.string.channels)
}

object Apps : Tab {
    override val route = "apps"

    override val title: String
        @Composable
        get() {
            return stringResource(id = R.string.your_apps)
        }
}

object ItemDetails : Destination {
    override val route: String = "item_details"
    const val channelIdArg = "channel_id"
    const val programIdArg = "program_id"

    val routeWithArgs = "$route/{$channelIdArg}/{$programIdArg}"
    val arguments = listOf(
        navArgument(channelIdArg) { type = NavType.LongType },
        navArgument(programIdArg) { type = NavType.LongType }
    )
}
