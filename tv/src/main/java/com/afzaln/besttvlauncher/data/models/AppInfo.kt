package com.afzaln.besttvlauncher.data.models

import android.content.Context
import android.content.Intent

data class AppInfo(
    val label: String,
    val packageName: String,
    val activityName: String,
)

fun AppInfo.getLaunchIntent(context: Context): Intent? {
    return context.packageManager.getLeanbackLaunchIntentForPackage(packageName)
}
