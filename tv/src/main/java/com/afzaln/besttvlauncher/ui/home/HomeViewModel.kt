package com.afzaln.besttvlauncher.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.afzaln.besttvlauncher.data.AppInfoRepository
import com.afzaln.besttvlauncher.data.SecondRepository

class HomeViewModel(appInfoRepo: AppInfoRepository, secondRepository: SecondRepository) : ViewModel() {

    val appInfoList = liveData {
        emit(appInfoRepo.getApps())
    }
}