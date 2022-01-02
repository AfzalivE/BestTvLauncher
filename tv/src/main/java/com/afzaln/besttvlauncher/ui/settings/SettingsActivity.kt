package com.afzaln.besttvlauncher.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class SettingsActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Display the fragment as the main content.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                android.R.id.content,
                SettingsContainerFragment()
            ).commit()
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}