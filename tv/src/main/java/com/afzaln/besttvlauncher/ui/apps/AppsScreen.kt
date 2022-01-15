package com.afzaln.besttvlauncher.ui.apps

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.data.AppInfo
import com.afzaln.besttvlauncher.data.getLaunchIntent
import com.afzaln.besttvlauncher.ui.settings.SettingsActivity
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.locatorViewModel
import kotlinx.coroutines.launch
import logcat.logcat

@OptIn(ExperimentalMaterial3Api::class)
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
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val relocationRequester = remember { BringIntoViewRequester() }

    val density = LocalDensity.current

    // LazyColumn(
    LazyVerticalGrid(
        cells = GridCells.Fixed(6),
        state = listState
    ) {
        val repeatList = mutableListOf<AppInfo>()
        repeat(40) {
            repeatList += appList
        }

        itemsIndexed(items = repeatList,
            spans = { index, item ->
                GridItemSpan(1)
            }) { index, appInfo ->
            AppCard(
                appInfo,
                modifier = Modifier.bringIntoViewRequester(relocationRequester),
                onFocus = {
                    val offset =
                        (listState.layoutInfo.viewportEndOffset - listState.layoutInfo.viewportStartOffset) / 2
                    logcat { "$index is focused, item height is $offset" }
//                    coroutineScope.launch {
//                        // relocationRequester.bringIntoView()
//                        listState.scrollToItem(index, scrollOffset = -offset)
//                    }
                }
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
                onFocus = onFocus
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

fun Modifier.dpadFocusable(
    unfocusedBorderWidth: Dp = 2.dp,
    focusedBorderWidth: Dp = 2.dp,
    unfocusedBorderColor: Color = Color.Black,
    focusedBorderColor: Color = Color.White,
    onFocus: () -> Unit
) = composed {
    val boxInteractionSource = remember { MutableInteractionSource() }
    val isItemFocused by boxInteractionSource.collectIsFocusedAsState()

    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isItemFocused) focusedBorderWidth else unfocusedBorderWidth
    )

    val infiniteTransition = rememberInfiniteTransition()
    val animatedFocusedBorder by infiniteTransition.animateColor(
        initialValue = focusedBorderColor.copy(alpha = 0.5f),
        targetValue = focusedBorderColor,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isItemFocused) animatedFocusedBorder else unfocusedBorderColor
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isItemFocused) 1.1f else 1f
    )

    // if (isItemFocused) {
    //     onFocus()
    // }

    this
        .focusable(interactionSource = boxInteractionSource)
        .scale(animatedScale)
        .border(
            width = animatedBorderWidth,
            color = animatedBorderColor,
            shape = AppTheme.cardShape
        )
}
