package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import androidx.tvprovider.media.tv.PreviewChannelHelper
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.PreviewProgramHelper

class ProgramRepository(context: Context) {
    private val packageManager: PackageManager = context.packageManager
    private val previewProgramHelper: PreviewProgramHelper = PreviewProgramHelper(context)

    fun getProgramsForChannel(channelId: Long): List<PreviewProgram> {
        return previewProgramHelper.getAllProgramsInChannel(channelId)
    }
}