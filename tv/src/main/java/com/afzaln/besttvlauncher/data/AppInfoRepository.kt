package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable

class AppInfoRepository(private val context: Context) {
    private val packageManager: PackageManager = context.packageManager

    fun getApps(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }

        return packageManager.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != context.packageName }
            .filter { packageManager.getLeanbackLaunchIntentForPackage(it.activityInfo.packageName) != null }
            // Exclude Timers & Clock on Sony which has two Activity definitions.
            .distinctBy { it.activityInfo.packageName + it.activityInfo.name.split(".").last() }
            .map { app ->
                AppInfo(
                    app.loadLabel(packageManager),
                    app.activityInfo.packageName,
                    app.activityInfo.loadBanner(packageManager) as BitmapDrawable
                )
            }
    }
}