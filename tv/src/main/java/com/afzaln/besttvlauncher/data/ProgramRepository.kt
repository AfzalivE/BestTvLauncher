package com.afzaln.besttvlauncher.data

import android.content.Context
import androidx.tvprovider.media.tv.WatchNextProgramHelper
import com.afzaln.besttvlauncher.data.models.Program
import com.afzaln.besttvlauncher.data.models.toProgram
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class ProgramRepository(context: Context) {
    private val watchNextProgramHelper = WatchNextProgramHelper(context)

    private val _watchNextPrograms = MutableStateFlow<ImmutableList<Program>>(persistentListOf())
    val watchNextPrograms = _watchNextPrograms.asStateFlow()

    suspend fun refreshData() {
        loadWatchNextPrograms()
    }

    // TODO observe ContentProvider using ContentResolver.registerContentObserver
    private suspend fun loadWatchNextPrograms() = withContext(Dispatchers.IO) {
        _watchNextPrograms.update {
            watchNextProgramHelper.allWatchNextPrograms.map { program ->
                program.toProgram()
            }.toImmutableList()
        }
    }
}
