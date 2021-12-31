package com.afzaln.besttvlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewChannelHelper
import androidx.tvprovider.media.tv.PreviewProgramHelper

class ChannelRepository(context: Context) {
    private val previewChannelHelper: PreviewChannelHelper = PreviewChannelHelper(context)

    val channels: List<PreviewChannel>
        get() = previewChannelHelper.allChannels
}