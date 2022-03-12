package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import androidx.tvprovider.media.tv.*

class ProgramRepository(context: Context) {
    private val watchNextProgramHelper = WatchNextProgramHelper(context)

    val watchNextPrograms: MutableList<WatchNextProgram>
        get() = watchNextProgramHelper.allWatchNextPrograms
}
