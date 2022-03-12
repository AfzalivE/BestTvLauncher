package com.afzaln.besttvlauncher.ui.apps

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_16_9
import com.afzaln.besttvlauncher.data.AppInfoRepository
import com.afzaln.besttvlauncher.data.ChannelRepository
import com.afzaln.besttvlauncher.data.ProgramRepository
import com.afzaln.besttvlauncher.data.UserPreferences
import kotlinx.coroutines.launch

class HomeViewModel(
    appInfoRepo: AppInfoRepository,
    private val channelRepository: ChannelRepository,
    private val programRepository: ProgramRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val appInfoList = appInfoRepo.apps.asLiveData()

    val wrappedChannelList = channelRepository.channels.asLiveData()

    val programsByChannel = channelRepository.channelProgramMap.asLiveData()

    fun loadData() {
        viewModelScope.launch {
            channelRepository.refreshData()
        }
    }

    val watchNextChannel = liveData {
        emit(programRepository.watchNextPrograms)
    }

    val selectedChannels: Set<String>
        get() = userPreferences.enabledChannels
}
