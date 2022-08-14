package com.afzaln.besttvlauncher.ui.apps

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.tv.foundation.lazy.grid.*
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.data.models.AppInfo
import com.afzaln.besttvlauncher.data.models.getLaunchIntent
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.dpadFocusable
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AppsScreen(state: HomeViewModel.State) {
    if (state is HomeViewModel.State.Loaded) {
        val appList = state.appInfoList
        Column {
            AppList(appList)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppList(appList: ImmutableList<AppInfo>) {
    val gridState = rememberTvLazyGridState()
    val relocationRequester = remember { BringIntoViewRequester() }

    TvLazyVerticalGrid(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        columns = TvGridCells.Fixed(6),
        state = gridState
    ) {
        val repeatList = mutableListOf<AppInfo>()
        repeat(10) {
            repeatList += appList
        }

        items(items = repeatList,
            span = { TvGridItemSpan(1) }) { appInfo ->
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
                bitmap = appInfo.banner.toBitmap().asImageBitmap(),
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
