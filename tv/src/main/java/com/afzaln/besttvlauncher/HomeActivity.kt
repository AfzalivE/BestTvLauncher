package com.afzaln.besttvlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.ui.BestTvApp
import com.afzaln.besttvlauncher.ui.theme.AppTheme

class HomeActivity : ComponentActivity() {
    lateinit var permissionRequester: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionRequester =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    showApp()
                } else {
                    showPermissionError()
                }
            }

        requestPermission()
    }

    private fun showPermissionError() = setContent {
        AppTheme {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "The READ_TV_LISTINGS permission is required to get recommendations from installed apps on this TV.")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {
                        requestPermission()
                    }) {
                        Text(text = "Click here to request permission")
                    }
                }
            }
        }
    }

    private fun requestPermission() = permissionRequester.launch(PERMISSION_READ_TV_LISTINGS)

    private fun showApp() = setContent {
        BestTvApp()
    }
}

const val PERMISSION_READ_TV_LISTINGS = "android.permission.READ_TV_LISTINGS"
