package com.afzaln.besttvlauncher.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferences(app: Application) {

    private val preferences: SharedPreferences =
        app.getSharedPreferences(USER_PREF_FILE_NAME, Context.MODE_PRIVATE)

    var enabledProviders: List<Long>
        get() = preferences.getStringSet(KEY_PROVIDERS, emptySet())!!.map { it.toLong() }
        set(value) {
            preferences.edit {
                val stringSet = value.map { it.toString() }.toSet()
                putStringSet(KEY_PROVIDERS, stringSet)
            }
        }

    companion object {
        const val USER_PREF_FILE_NAME = "user_preferences"
        const val KEY_PROVIDERS = "pref_providers"
    }
}
