package com.afzaln.besttvlauncher.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.afzaln.besttvlauncher.data.models.AppInfo
import com.afzaln.besttvlauncher.data.models.getLaunchIntent
import com.afzaln.besttvlauncher.image.toPackageUri
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

@Composable
fun AppList(appList: ImmutableList<AppInfo>) {
    val gridState = rememberTvLazyGridState()

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
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(appInfo.toPackageUri())
                    .build(),
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
        // TODO provide a slot API for the image
//        AppCard(
//            AppInfo(
//                label = "App name",
//                packageName = "com.afzaln.example",
//                activityName = BitmapDrawable(
//                    resources,
//                    BitmapFactory.decodeResource(
//                        resources,
//                        R.drawable.app_icon_your_company
//                    )
//                )
//            ),
//            onFocus = {
//
//            }
//        )
    }
}
