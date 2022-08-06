package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AppInfoRepository(private val context: Context) {
    private val packageManager: PackageManager = context.packageManager

    private var cached: List<AppInfo> = listOf()
    val apps: Flow<List<AppInfo>> = flow {
        emit(cached)
        val newList = loadAppInfo()
        if (cached != newList) {
            cached = newList
            emit(cached)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun loadAppInfo(): List<AppInfo> = withContext(Dispatchers.IO) {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }

        packageManager.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != context.packageName }
            .filter { packageManager.getLeanbackLaunchIntentForPackage(it.activityInfo.packageName) != null }
            // Exclude Timers & Clock on Sony which has two Activity definitions.
            .distinctBy { it.activityInfo.packageName + it.activityInfo.name.split(".").last() }
            .map { app ->
                AppInfo(
                    app.loadLabel(packageManager),
                    app.activityInfo.packageName,
                    app.activityInfo.loadBanner(packageManager)
                )
            }
    }
}
