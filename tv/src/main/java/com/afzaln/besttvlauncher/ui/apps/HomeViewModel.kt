package com.afzaln.besttvlauncher.ui.apps

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
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
        get() = Transformations.map(wrappedChannelList) { listOfChannels ->
            val map = listOfChannels.map { it.channel }.associateWith {
                programRepository.getProgramsForChannel(it.id)
            }

            return@map map
        }

    val selectedChannels = userPreferences.enabledChannels
}