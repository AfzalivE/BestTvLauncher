package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable

data class AppInfo(
    val label: CharSequence,
    val packageName: String,
    val banner: Drawable
)

fun AppInfo.getLaunchIntent(context: Context): Intent? {
    return context.packageManager.getLeanbackLaunchIntentForPackage(packageName)
}
