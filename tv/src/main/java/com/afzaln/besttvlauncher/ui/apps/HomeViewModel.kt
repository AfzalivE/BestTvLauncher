package com.afzaln.besttvlauncher.ui.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.afzaln.besttvlauncher.data.AppInfoRepository
import com.afzaln.besttvlauncher.data.ChannelRepository
import com.afzaln.besttvlauncher.data.ProgramRepository
import com.afzaln.besttvlauncher.data.UserPreferences
import com.afzaln.besttvlauncher.data.models.AppInfo
import com.afzaln.besttvlauncher.data.models.Channel
import com.afzaln.besttvlauncher.data.models.Program
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
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
            val programsByChannel: ImmutableMap<Channel, ImmutableList<Program>>,
            val watchNextPrograms: ImmutableList<Program>,
            val appInfoList: ImmutableList<AppInfo>
        ) : State()
    }
}
