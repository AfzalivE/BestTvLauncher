package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class AppInfoRepository(context: Context) {
    private val packageManager: PackageManager = context.packageManager

    fun getApps(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }

        return packageManager.queryIntentActivities(intent, 0).map { app ->
            AppInfo(
                app.loadLabel(packageManager),
                app.activityInfo.packageName,
                app.activityInfo.loadBanner(packageManager)
            )
        }
    }
}