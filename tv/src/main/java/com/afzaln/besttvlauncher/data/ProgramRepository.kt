package com.afzaln.besttvlauncher.data

import android.content.Context
import androidx.tvprovider.media.tv.WatchNextProgram
import androidx.tvprovider.media.tv.WatchNextProgramHelper

class ProgramRepository(context: Context) {
    private val watchNextProgramHelper = WatchNextProgramHelper(context)

    val watchNextPrograms: MutableList<WatchNextProgram>
        get() = watchNextProgramHelper.allWatchNextPrograms
}
