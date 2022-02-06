package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import androidx.tvprovider.media.tv.*

class ProgramRepository(context: Context) {
    private val previewProgramHelper: PreviewProgramHelper = PreviewProgramHelper(context)
    private val watchNextProgramHelper = WatchNextProgramHelper(context)

    fun getProgramsForChannel(channelId: Long): List<PreviewProgram> {
        return previewProgramHelper.getAllProgramsInChannel(channelId)
    }

    val watchNextPrograms: MutableList<WatchNextProgram>
        get() = watchNextProgramHelper.allWatchNextPrograms
}
