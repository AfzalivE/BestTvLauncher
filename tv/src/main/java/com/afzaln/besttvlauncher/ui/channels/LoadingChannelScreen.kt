package com.afzaln.besttvlauncher.ui.channels

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.placeholder

@Composable
fun LoadingChannelScreen() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(3) {
            Row(
                Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                repeat(10) {
                    LoadingProgramCard()
                }
            }
        }
    }
}

@Composable
fun LoadingProgramCard() {
    Column(
        modifier = Modifier
            .requiredWidth(120.dp)
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Card(
            shape = AppTheme.cardShape, modifier = Modifier
                .requiredHeight(120.dp)
                .placeholder(
                    visible = true,
                    color = Color.DarkGray,
                    shape = AppTheme.cardShape,
                    highlight = PlaceholderHighlight.fade(highlightColor = Color.Gray)
                )
        ) {
            Box(modifier = Modifier.fillMaxSize())
        }

        Text(
            "Empty text",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .width(160.dp)
                .requiredHeight(48.dp)
                .padding(top = 16.dp)
                .placeholder(
                    visible = true,
                    color = Color.DarkGray,
                    shape = AppTheme.cardShape,
                    highlight = PlaceholderHighlight.fade(highlightColor = Color.Gray)
                )
        )
    }
}
