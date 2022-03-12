package com.afzaln.besttvlauncher.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.tvprovider.media.tv.*
import kotlinx.coroutines.flow.*

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

        channels.collect { channelList ->
            _cachedMap.update { map ->
                return@update loadPrograms(channelList, map)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun loadPrograms(
        channelList: List<PreviewChannelWrapper>,
        initial: Map<PreviewChannel, List<PreviewProgram>>
    ): Map<PreviewChannel, List<PreviewProgram>> = initial.toMutableMap().apply {
        channelList.forEach { wrappedChannel ->
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

    /**
     * To compensate for YouTube Recommended, Music,
     * and Trending art ratios including black bars.
     */
    private fun isWrongAspectRatio(channel: PreviewChannel) =
        channel.packageName == "com.google.android.youtube.tv" &&
                !channel.displayName.contentEquals("Free movies from Youtube", true)

    private fun loadChannels() {
        _channels.update {
            return@update previewChannelHelper.allChannels.map { previewChannel ->
                val appInfo = packageManager.getApplicationInfo(previewChannel.packageName, 0)
                PreviewChannelWrapper(
                    previewChannel,
                    appInfo.loadLabel(packageManager).toString()
                )
            }
        }
    }
}
