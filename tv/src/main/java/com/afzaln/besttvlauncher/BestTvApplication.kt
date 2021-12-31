package com.afzaln.besttvlauncher

import android.app.Application
import com.afzaln.besttvlauncher.core.Locator
import logcat.logcat

class BestTvApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Locator.init(this)
        logcat { "Initialized locator" }
    }
}