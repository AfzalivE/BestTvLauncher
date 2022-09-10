package com.afzaln.besttvlauncher

import android.app.Application
import com.afzaln.besttvlauncher.core.Locator
import com.afzaln.besttvlauncher.image.AppInfoImageLoaderFactory
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import logcat.logcat

class BestTvApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Locator.init(this)
        AppInfoImageLoaderFactory(this).init()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.DEBUG)
        logcat { "Initialized locator" }
    }
}
