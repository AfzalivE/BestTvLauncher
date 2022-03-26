package com.afzaln.besttvlauncher.ui.apps

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import androidx.palette.graphics.Palette
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

    val palette = MutableLiveData<Palette>()
    val backgroundColor = MediatorLiveData<Color>().apply {
        addSource(palette) { palette ->
            palette.vibrantSwatch?.rgb?.let {
                value = Color(it).copy(0.05f)
            }
        }
    }

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
