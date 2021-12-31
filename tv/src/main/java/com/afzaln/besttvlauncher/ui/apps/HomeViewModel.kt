package com.afzaln.besttvlauncher.ui.apps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import com.afzaln.besttvlauncher.data.AppInfoRepository
import com.afzaln.besttvlauncher.data.ChannelRepository
import com.afzaln.besttvlauncher.data.ProgramRepository

class HomeViewModel(
    appInfoRepo: AppInfoRepository,
    channelRepository: ChannelRepository,
    private val programRepository: ProgramRepository
) : ViewModel() {

    val appInfoList = liveData {
        emit(appInfoRepo.apps)
    }

    val channelList = liveData {
        emit(channelRepository.channels)
    }

    val programsByChannel: LiveData<Map<PreviewChannel, List<PreviewProgram>>>
        get() = Transformations.map(channelList) { listOfChannels ->
            listOfChannels.associateWith {
                programRepository.getProgramsForChannel(it.id)
            }
        }
}