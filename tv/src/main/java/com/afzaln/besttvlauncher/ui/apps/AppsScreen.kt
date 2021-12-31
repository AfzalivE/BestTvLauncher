package com.afzaln.besttvlauncher.ui.apps

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.data.AppInfo
import com.afzaln.besttvlauncher.data.getLaunchIntent
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.locatorViewModel

@Composable
fun AppsScreen() {
    val viewModel: HomeViewModel = locatorViewModel()
    val appList by viewModel.appInfoList.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .padding(top = 27.dp, bottom = 27.dp)
    ) {
        AppsTitle()
        AppList(appList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppList(appList: List<AppInfo>) {
    LazyVerticalGrid(cells = GridCells.Fixed(6)) {
        items(appList) { appInfo ->
            AppCard(appInfo)
        }
    }
}

@Composable
fun AppCard(appInfo: AppInfo) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .dpadFocusable(
                unfocusedBorderColor = MaterialTheme.colorScheme.background
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
                contentDescription = "Icon for ${appInfo.label}"
            )
        }
    }
}

@Composable
fun AppsTitle() {
    Text(text = stringResource(id = R.string.your_apps))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        AppsTitle()
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
            )
        )
    }
}

fun Modifier.dpadFocusable(
    unfocusedBorderWidth: Dp = 2.dp,
    focusedBorderWidth: Dp = 2.dp,
    unfocusedBorderColor: Color = Color.Black,
    focusedBorderColor: Color = Color.White
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

    val animatedScale by animateFloatAsState(
        targetValue = if (isItemFocused) 1.1f else 1f
    )

    this
        .focusable(interactionSource = boxInteractionSource)
        .scale(animatedScale)
        .border(
            width = animatedBorderWidth,
            color = if (isItemFocused) animatedFocusedBorder else unfocusedBorderColor,
            shape = AppTheme.cardShape
        )
}