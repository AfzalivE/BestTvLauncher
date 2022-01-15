package com.afzaln.besttvlauncher.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferences(app: Application) {

    private val preferences: SharedPreferences =
        app.getSharedPreferences(USER_PREF_FILE_NAME, Context.MODE_PRIVATE)

    var enabledChannels: Set<String>
        get() = preferences.getStringSet(KEY_CHANNELS, emptySet())!!.toSet()
        set(value) {
            preferences.edit {
                putStringSet(KEY_CHANNELS, value)
            }
        }

    companion object {
        const val USER_PREF_FILE_NAME = "user_preferences"
        const val KEY_CHANNELS = "pref_channels"
    }
}
