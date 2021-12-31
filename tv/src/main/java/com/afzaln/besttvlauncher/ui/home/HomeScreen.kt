package com.afzaln.besttvlauncher.ui.home

import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.data.AppInfo
import com.afzaln.besttvlauncher.ui.theme.BestTvLauncherTheme
import com.afzaln.besttvlauncher.utils.locatorViewModel

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = locatorViewModel()
    val appList by viewModel.appInfoList.observeAsState(emptyList())

    Column {
        AppCount(appList.size)
        AppList(appList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppList(appList: List<AppInfo>) {
    LazyVerticalGrid(cells = GridCells.Adaptive(128.dp)) {
        items(appList) { appInfo ->
            AppCard(appInfo)
        }
    }
}

@Composable
fun AppCard(appInfo: AppInfo) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.clickable {
            val intent =
                context.packageManager.getLaunchIntentForPackage(appInfo.packageName.toString())
            context.startActivity(intent)
        }
    ) {
        Card {
            if (appInfo.banner is BitmapDrawable) {
                Image(
                    bitmap = appInfo.banner.bitmap.asImageBitmap(),
                    contentDescription = "Icon for ${appInfo.label}"
                )
            } else {
                throw IllegalArgumentException("Invalid banner type: ${appInfo.banner}")
            }
        }
        Text("${appInfo.label}")
    }
}

@Composable
fun AppCount(count: Int) {
    Text(text = "$count apps")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BestTvLauncherTheme {
        AppCount(12)
    }
}