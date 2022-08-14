package com.afzaln.besttvlauncher.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.tvprovider.media.tv.PreviewChannelHelper
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.PreviewProgramHelper
import androidx.tvprovider.media.tv.TvContractCompat
import com.afzaln.besttvlauncher.data.models.Channel
import com.afzaln.besttvlauncher.data.models.Program
import com.afzaln.besttvlauncher.data.models.toProgram
import kotlinx.collections.immutable.*
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

    private var _cachedMap = MutableStateFlow<ImmutableMap<Channel, ImmutableList<Program>>>(
        persistentMapOf()
    )
    val channelProgramMap = _cachedMap.asStateFlow()

    private val _channels = MutableStateFlow<ImmutableList<Channel>>(persistentListOf())
    val channels = _channels.asStateFlow()

    suspend fun refreshData() {
        loadChannels()

        _cachedMap.update { map ->
            return@update loadPrograms(channels.value, map)
        }
    }

    @SuppressLint("RestrictedApi")
    private suspend fun loadPrograms(
        channelList: ImmutableList<Channel>,
        initial: ImmutableMap<Channel, ImmutableList<Program>>
    ): ImmutableMap<Channel, ImmutableList<Program>> = withContext(Dispatchers.IO) {
        initial.toMutableMap().apply {
            channelList.forEach { channel ->
                logcat { "LoadPrograms: ${Thread.currentThread().name}" }
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
                }.map { program ->
                    program.toProgram()
                }.toImmutableList()
                put(channel, programs)
            }
        }.toImmutableMap()
    }

    /**
     * To compensate for YouTube Recommended, Music,
     * and Trending art ratios including black bars.
     */
    private fun isWrongAspectRatio(channel: Channel) =
        channel.packageName == "com.google.android.youtube.tv" &&
                !channel.displayName.contentEquals("Free movies from Youtube", true)

    private suspend fun loadChannels() {
        withContext(Dispatchers.IO) {
            logcat { "LoadChannels: ${Thread.currentThread().name}" }
            _channels.update {
                return@update previewChannelHelper.allChannels.map { previewChannel ->
                    logcat { "LoadChannels: ${Thread.currentThread().name}" }
                    val appInfo = packageManager.getApplicationInfo(previewChannel.packageName, 0)
                    Channel(
                        previewChannel.id,
                        previewChannel.displayName.toString(),
                        previewChannel.packageName,
                        appInfo.loadLabel(packageManager).toString()
                    )
                }.toImmutableList()
            }
        }
    }
}
