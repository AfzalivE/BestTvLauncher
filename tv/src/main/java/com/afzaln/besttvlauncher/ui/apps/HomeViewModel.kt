package com.afzaln.besttvlauncher.ui.apps

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_16_9
import androidx.tvprovider.media.tv.WatchNextProgram
import com.afzaln.besttvlauncher.data.AppInfoRepository
import com.afzaln.besttvlauncher.data.ChannelRepository
import com.afzaln.besttvlauncher.data.ProgramRepository
import com.afzaln.besttvlauncher.data.UserPreferences

class HomeViewModel(
    appInfoRepo: AppInfoRepository,
    channelRepository: ChannelRepository,
    private val programRepository: ProgramRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val appInfoList = liveData {
        emit(appInfoRepo.apps)
    }

    val wrappedChannelList = liveData {
        emit(channelRepository.channels)
    }

    val programsByChannel: LiveData<Map<PreviewChannel, List<PreviewProgram>>>
        @SuppressLint("RestrictedApi")
        get() = Transformations.map(wrappedChannelList) { listOfChannels ->
            val map = listOfChannels.map { it.channel }.associateWith {
                if (it.packageName == "com.google.android.youtube.tv" &&
                    !it.displayName.contentEquals("Free movies from Youtube", true)
                ) {
                    // To compensate for YouTube Recommended, Music,
                    // and Trending art ratios including black bars.
                    programRepository.getProgramsForChannel(it.id).map { program ->
                        PreviewProgram.Builder(program)
                            .setPosterArtAspectRatio(ASPECT_RATIO_16_9)
                            .build()
                    }
                } else {
                    programRepository.getProgramsForChannel(it.id)
                }
            }

            return@map map
        }

    val watchNextChannel = liveData {
        emit(programRepository.watchNextPrograms)
    }

    val selectedChannels: Set<String>
        get() = userPreferences.enabledChannels
}
