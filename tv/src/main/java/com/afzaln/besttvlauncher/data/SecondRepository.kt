package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable

class SecondRepository(context: Context) {
    private val packageManager: PackageManager = context.packageManager

    fun getApps(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val allApps = packageManager.queryIntentActivities(intent, 0)
        return allApps.map { app ->
            AppInfo(
                app.loadLabel(packageManager),
                app.activityInfo.packageName,
                app.activityInfo.loadBanner(packageManager) as BitmapDrawable
            )
        }
    }
}