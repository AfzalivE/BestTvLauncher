package com.afzaln.besttvlauncher.ui.apps

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import androidx.palette.graphics.Palette
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.WatchNextProgram
import com.afzaln.besttvlauncher.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    appInfoRepo: AppInfoRepository,
    private val channelRepository: ChannelRepository,
    private val programRepository: ProgramRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
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

    val state = combine(
        channelRepository.channelProgramMap,
        programRepository.watchNextPrograms,
        appInfoRepo.apps
    ) { channelProgramMap, watchNextPrograms, appInfoList ->
        if (channelProgramMap.isEmpty() || appInfoList.isEmpty()) {
            State.Loading
        } else {
            State.Loaded(channelProgramMap, watchNextPrograms, appInfoList)
        }
    }

    fun loadData() {
        viewModelScope.launch {
            delay(500)
            channelRepository.refreshData()
            programRepository.refreshData()
        }
    }

    val selectedChannels: Set<String>
        get() = userPreferences.enabledChannels

    sealed class State {
        object Loading : State()
        class Loaded(
            val programsByChannel: Map<PreviewChannel, List<PreviewProgram>>,
            val watchNextPrograms: List<WatchNextProgram>,
            val appInfoList: List<AppInfo>
        ) : State()
    }
}
