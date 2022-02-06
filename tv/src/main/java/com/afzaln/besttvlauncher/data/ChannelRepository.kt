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
    private val packageManager: PackageManager = context.packageManager

    val channels: List<PreviewChannelWrapper>
        get() {
            return previewChannelHelper.allChannels.map { previewChannel ->
                val appInfo = packageManager.getApplicationInfo(previewChannel.packageName, 0)
                PreviewChannelWrapper(
                    previewChannel,
                    appInfo.loadLabel(packageManager).toString()
                )
            }
        }
}
