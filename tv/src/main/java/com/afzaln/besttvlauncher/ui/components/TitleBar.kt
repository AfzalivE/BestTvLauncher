package com.afzaln.besttvlauncher.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.ui.Apps
import com.afzaln.besttvlauncher.ui.Channels
import com.afzaln.besttvlauncher.ui.Tab
import com.afzaln.besttvlauncher.ui.theme.AppTheme

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
