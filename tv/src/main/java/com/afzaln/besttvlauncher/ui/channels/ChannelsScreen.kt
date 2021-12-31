package com.afzaln.besttvlauncher.ui.channels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import coil.compose.rememberImagePainter
import com.afzaln.besttvlauncher.ui.apps.AppsTitle
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.apps.dpadFocusable
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.locatorViewModel

@Composable
fun ChannelsScreen() {
    val viewModel: HomeViewModel = locatorViewModel()
    val programList by viewModel.programsByChannel.observeAsState(emptyMap())

    Column(
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .padding(top = 27.dp, bottom = 27.dp)
    ) {
        AppsTitle()
        ChannelList(programList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelList(programMap: Map<PreviewChannel, List<PreviewProgram>>) {
    val channels = programMap.keys.toList()
    LazyColumn {
        items(channels) { channel ->
            ChannelRow(channel, programMap[channel] ?: emptyList())
        }
    }
}

@Composable
fun ChannelRow(channel: PreviewChannel, programs: List<PreviewProgram>) {
    if (programs.isEmpty()) return

    Column {
        Text(
            text = channel.displayName.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        LazyRow {
            items(programs) { program ->
                Column {
                    Card(shape = AppTheme.cardShape) {
                        Image(
                            painter = rememberImagePainter(data = program.thumbnailUri),
                            contentDescription = "Thumbnail for for ${program.title}",
                            modifier = Modifier.size(128.dp)
                        )
                    }
                    Text(
                        text = program.title,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun ChannelCard(channel: PreviewChannel) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .dpadFocusable(
                unfocusedBorderColor = MaterialTheme.colorScheme.background
            )
            .clickable {
                val intent = channel.appLinkIntent
                if (intent != null) {
                    context.startActivity(intent)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(shape = AppTheme.cardShape) {
            Image(
                bitmap = channel.getLogo(context).asImageBitmap(),
                contentDescription = "Image for ${channel.displayName}"
            )
        }
        Text(
            color = MaterialTheme.colorScheme.onBackground,
            text = "${channel.displayName}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )
    }
}