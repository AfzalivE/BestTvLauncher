package com.afzaln.besttvlauncher.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.tvprovider.media.tv.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import logcat.logcat

class ChannelRepository(context: Context) {
    private val previewChannelHelper: PreviewChannelHelper = PreviewChannelHelper(context)
    private val previewProgramHelper: PreviewProgramHelper = PreviewProgramHelper(context)
    private val packageManager: PackageManager = context.packageManager

    private var _cachedMap = MutableStateFlow<Map<PreviewChannel, List<PreviewProgram>>>(mapOf())
    val channelProgramMap = _cachedMap.asStateFlow()

    private val _channels = MutableStateFlow<List<PreviewChannelWrapper>>(listOf())
    val channels = _channels.asStateFlow()

    suspend fun refreshData() {
        loadChannels()

        _cachedMap.update { map ->
            return@update loadPrograms(channels.value, map)
        }
    }

    @SuppressLint("RestrictedApi")
    private suspend fun loadPrograms(
        channelList: List<PreviewChannelWrapper>,
        initial: Map<PreviewChannel, List<PreviewProgram>>
    ): Map<PreviewChannel, List<PreviewProgram>> = withContext(Dispatchers.IO) {
        initial.toMutableMap().apply {
            channelList.forEach { wrappedChannel ->
                logcat { "LoadPrograms: ${Thread.currentThread().name}" }
                val channel = wrappedChannel.channel
                val programs = if (isWrongAspectRatio(channel)) {
                    previewProgramHelper.getAllProgramsInChannel(channel.id).map { program ->
                        PreviewProgram.Builder(program)
                            .setPosterArtAspectRatio(
                                TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_16_9
                            )
                            .build()
                    }
                } else {
                    previewProgramHelper.getAllProgramsInChannel(channel.id)
                }
                put(channel, programs)
            }
        }
    }

    /**
     * To compensate for YouTube Recommended, Music,
     * and Trending art ratios including black bars.
     */
    private fun isWrongAspectRatio(channel: PreviewChannel) =
        channel.packageName == "com.google.android.youtube.tv" &&
                !channel.displayName.contentEquals("Free movies from Youtube", true)

    private suspend fun loadChannels() {
        withContext(Dispatchers.IO) {
            logcat { "LoadChannels: ${Thread.currentThread().name}" }
            _channels.update {
                return@update previewChannelHelper.allChannels.map { previewChannel ->
                    logcat { "LoadChannels: ${Thread.currentThread().name}" }
                    val appInfo = packageManager.getApplicationInfo(previewChannel.packageName, 0)
                    PreviewChannelWrapper(
                        previewChannel,
                        appInfo.loadLabel(packageManager).toString()
                    )
                }
            }
        }
    }
}
