package com.afzaln.besttvlauncher.ui.apps

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.data.AppInfo
import com.afzaln.besttvlauncher.data.getLaunchIntent
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.dpadFocusable
import com.afzaln.besttvlauncher.utils.locatorViewModel
import logcat.logcat

@Composable
fun AppsScreen() {
    val viewModel: HomeViewModel = locatorViewModel()
    val appList by viewModel.appInfoList.observeAsState(emptyList())

    Column {
        AppList(appList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppList(appList: List<AppInfo>) {
    val gridState = rememberLazyGridState()
    val relocationRequester = remember { BringIntoViewRequester() }

    LazyVerticalGrid(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        columns = GridCells.Fixed(6),
        state = gridState
    ) {
        val repeatList = mutableListOf<AppInfo>()
        repeat(5) {
            repeatList += appList
        }

        items(items = repeatList,
            span = { GridItemSpan(1) }) { appInfo ->
            AppCard(
                appInfo,
                modifier = Modifier.bringIntoViewRequester(relocationRequester),
                onFocus = {}
            )
        }
    }
}

@Composable
fun AppCard(
    appInfo: AppInfo,
    onFocus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .requiredHeight(120.dp)
            .requiredWidth(120.dp)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .onFocusChanged {
                if (it.isFocused) {
                    onFocus()
                }
            }
            .dpadFocusable(
                unfocusedBorderColor = MaterialTheme.colorScheme.background,
                onFocus = { onFocus() }
            )
            .clickable {
                val intent = appInfo.getLaunchIntent(context)
                if (intent != null) {
                    context.startActivity(intent)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(shape = AppTheme.cardShape) {
            Image(
                bitmap = appInfo.banner.bitmap.asImageBitmap(),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Icon for ${appInfo.label}"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultAppCard() {
    AppTheme {
        val resources = LocalContext.current.resources
        AppCard(
            AppInfo(
                label = "App name",
                packageName = "com.afzaln.example",
                banner = BitmapDrawable(
                    resources,
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.app_icon_your_company
                    )
                )
            ),
            onFocus = {

            }
        )
    }
}
