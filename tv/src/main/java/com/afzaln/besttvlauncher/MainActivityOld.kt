package com.afzaln.besttvlauncher

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * Loads [MainFragment].
 */
class MainActivityOld : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}