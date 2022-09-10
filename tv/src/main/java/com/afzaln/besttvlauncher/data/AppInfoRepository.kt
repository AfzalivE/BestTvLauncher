package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.afzaln.besttvlauncher.data.models.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AppInfoRepository(private val context: Context) {
    private val packageManager: PackageManager = context.packageManager

    private var cached: ImmutableList<AppInfo> = persistentListOf()
    val apps: Flow<ImmutableList<AppInfo>> = flow {
        emit(cached)
        val newList = loadAppInfo()
        if (cached != newList) {
            cached = newList
            emit(cached)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun loadAppInfo(): ImmutableList<AppInfo> = withContext(Dispatchers.IO) {
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
                    app.loadLabel(packageManager).toString(),
                    app.activityInfo.packageName,
                    app.activityInfo.name,
                )
            }.toImmutableList()
    }
}
