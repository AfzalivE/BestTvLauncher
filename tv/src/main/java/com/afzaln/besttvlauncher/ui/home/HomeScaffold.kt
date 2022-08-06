package com.afzaln.besttvlauncher.ui.home

import android.app.StatusBarManager
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.ui.Apps
import com.afzaln.besttvlauncher.ui.Channels
import com.afzaln.besttvlauncher.ui.Tab
import com.afzaln.besttvlauncher.ui.settings.SettingsActivity
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.expand
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    selectedTab: Tab,
    tabs: List<Tab>,
    containerColor: Color,
    onTabSelected: (Tab) -> Unit,
    content: @Composable () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val statusBarManager = context.getSystemService(StatusBarManager::class.java)

    Scaffold(
        containerColor = containerColor,
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                backgroundColor = containerColor,
                title = {
                    TitleBar(
                        selectedTab = selectedTab,
                        tabs = tabs,
                        onTabSelected = {
                            coroutineScope.launch {
                                onTabSelected(it)
                            }
                        }
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier.focusRequester(focusRequester),
                        onClick = {
                            context.startActivity(SettingsActivity.createIntent(context))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(
                                id = R.string.settings_title
                            )
                        )
                    }

                    IconButton(
                        onClick = {
                            statusBarManager.expand()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = stringResource(
                                id = R.string.notifications_title
                            )
                        )
                    }
                }
            )
        },
        content = {
            content()
        }
    )

    SideEffect {
        // focusRequester.requestFocus()
    }
}

@Composable
fun TitleBar(
    selectedTab: Tab,
    tabs: List<Tab>,
    onTabSelected: (Tab) -> Unit
) {
    val tabRowWidth = (tabs.size * 160).dp

    TabRow(
        modifier = Modifier.requiredWidth(tabRowWidth),
        indicator = {},
        divider = {},
        selectedTabIndex = tabs.indexOf(selectedTab),
        containerColor = Color.Transparent
    ) {
        tabs.forEach { tab ->
            TabItem(
                title = tab.title,
                selected = selectedTab == tab,
                onTabSelected = {
                    onTabSelected(tab)
                }
            )
        }
    }
}

@Composable
fun TabItem(
    title: String,
    selected: Boolean = false,
    onTabSelected: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isItemFocused by interactionSource.collectIsFocusedAsState()
    val isFocusedOrSelected = isItemFocused || selected

    val animatedBackground by animateColorAsState(
        animationSpec = tween(500),
        targetValue = if (isFocusedOrSelected) {
            MaterialTheme.colorScheme.inverseSurface
        } else {
            Color.Transparent
        }
    )

    val animatedTextColor by animateColorAsState(
        animationSpec = tween(500),
        targetValue = if (isFocusedOrSelected) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    )

    Tab(selected = selected,
        interactionSource = interactionSource,
        modifier = Modifier
            .background(
                color = animatedBackground,
                shape = RoundedCornerShape(32.dp)
            ),
        onClick = onTabSelected,
        text = {
            Text(
                text = title,
                color = animatedTextColor
            )
        }
    )

    if (isItemFocused) {
        onTabSelected()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        TitleBar(Channels, listOf(Channels, Apps)) {}
    }
}
