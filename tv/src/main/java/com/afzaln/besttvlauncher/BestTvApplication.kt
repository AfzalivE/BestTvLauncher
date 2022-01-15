package com.afzaln.besttvlauncher

import android.app.Application
import com.afzaln.besttvlauncher.core.Locator
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import logcat.logcat

class BestTvApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Locator.init(this)
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.DEBUG)
        logcat { "Initialized locator" }
    }
}