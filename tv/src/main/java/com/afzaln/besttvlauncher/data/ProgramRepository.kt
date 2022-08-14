package com.afzaln.besttvlauncher.data

import android.content.Context
import androidx.tvprovider.media.tv.WatchNextProgram
import androidx.tvprovider.media.tv.WatchNextProgramHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class ProgramRepository(context: Context) {
    private val watchNextProgramHelper = WatchNextProgramHelper(context)

    private val _watchNextPrograms = MutableStateFlow<List<WatchNextProgram>>(listOf())
    val watchNextPrograms = _watchNextPrograms.asStateFlow()

    suspend fun refreshData() {
        loadWatchNextPrograms()
    }

    // TODO observe ContentProvider using ContentResolver.registerContentObserver
    private suspend fun loadWatchNextPrograms() = withContext(Dispatchers.IO) {
        _watchNextPrograms.update {
            watchNextProgramHelper.allWatchNextPrograms
        }
    }
}
