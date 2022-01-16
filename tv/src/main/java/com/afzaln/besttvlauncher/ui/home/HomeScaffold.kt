package com.afzaln.besttvlauncher.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.ui.settings.SettingsActivity
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomeScaffold(
    pages: List<@Composable () -> Unit>,
) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    TitleBar(
                        selectedTabIndex = pagerState.currentPage,
                        onTabSelected = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(it)
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
                }
            )
        },
        content = {
            HorizontalPager(
                count = pages.size,
                state = pagerState
            ) {
                pages[pagerState.currentPage]()
            }
        }
    )

    SideEffect {
        // focusRequester.requestFocus()
    }
}

@Composable
fun TitleBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabList = listOf(
        R.string.channels,
        R.string.your_apps
    )
    val tabRowWidth = (tabList.size * 160).dp

    TabRow(
        modifier = Modifier.requiredWidth(tabRowWidth),
        indicator = {},
        selectedTabIndex = selectedTabIndex,
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        tabList.forEachIndexed { index, tabTitle ->
            val backgroundColor = if (selectedTabIndex == index) {
                MaterialTheme.colorScheme.inverseSurface
            } else {
                MaterialTheme.colorScheme.surface
            }

            val textColor = if (selectedTabIndex == index) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.onSurface
            }

            Tab(selected = selectedTabIndex == index,
                modifier = Modifier.background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(32.dp)
                ),
                onClick = {
                    onTabSelected(index)
                },
                text = {
                    Text(
                        text = stringResource(id = tabTitle),
                        color = textColor
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        TitleBar(0) {}
    }
}
