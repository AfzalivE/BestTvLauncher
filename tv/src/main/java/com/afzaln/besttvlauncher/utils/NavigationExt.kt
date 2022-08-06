package com.afzaln.besttvlauncher.utils

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.afzaln.besttvlauncher.ui.ItemDetails

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

fun NavHostController.navigateToItemDetails(channelId: Long, programId: Long) {
    this.navigate("${ItemDetails.route}/$channelId/$programId")
}
