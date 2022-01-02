package com.afzaln.besttvlauncher.core

import android.app.Application
import com.afzaln.besttvlauncher.BestTvApplication
import com.afzaln.besttvlauncher.data.AppInfoRepository
import com.afzaln.besttvlauncher.data.ChannelRepository
import com.afzaln.besttvlauncher.data.ProgramRepository
import com.afzaln.besttvlauncher.data.UserPreferences

object Locator : BaseLocator {
    private lateinit var app: Application
    override val locatorMap: MutableMap<Class<*>, Any> = mutableMapOf()

    fun init(app: BestTvApplication) {
        this.app = app
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    override fun <T : Any> create(clz: Class<T>): T = when (clz) {
        AppInfoRepository::class.java -> createAppInfoRepository()
        ChannelRepository::class.java -> createChannelRepository()
        ProgramRepository::class.java -> createProgramRepository()
        UserPreferences::class.java -> createUserPreferences()
        else -> throw IllegalArgumentException("unsupported class: $clz")
    } as T

    private fun createChannelRepository(): ChannelRepository = ChannelRepository(app)
    private fun createProgramRepository(): ProgramRepository = ProgramRepository(app)
    private fun createAppInfoRepository(): AppInfoRepository = AppInfoRepository(app)
    private fun createUserPreferences(): UserPreferences = UserPreferences(app)
}