package com.afzaln.besttvlauncher

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import com.afzaln.besttvlauncher.ui.BestTvApp

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BestTvApp()
        }

        val permissionRequester =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (!granted) {
                    Toast.makeText(
                        this,
                        "Cannot show recommendations without permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        permissionRequester.launch(PERMISSION_READ_TV_LISTINGS)
    }
}

const val PERMISSION_READ_TV_LISTINGS = "android.permission.READ_TV_LISTINGS"