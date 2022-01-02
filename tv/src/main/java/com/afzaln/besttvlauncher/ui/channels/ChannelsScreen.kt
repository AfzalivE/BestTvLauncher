package com.afzaln.besttvlauncher.ui.channels

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import com.afzaln.besttvlauncher.ui.apps.TitleBar
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.apps.dpadFocusable
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.locatorViewModel

@Composable
fun ChannelsScreen() {
    val viewModel: HomeViewModel = locatorViewModel()
    val programList by viewModel.programsByChannel.observeAsState(emptyMap())

    ChannelsScreenContent(programList)
}

@Composable
private fun ChannelsScreenContent(programList: Map<PreviewChannel, List<PreviewProgram>>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .padding(top = 27.dp, bottom = 27.dp)
    ) {
        TitleBar()
        ChannelList(programList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelList(programMap: Map<PreviewChannel, List<PreviewProgram>>) {
    val channels = programMap.keys.toList()
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        channels.forEach { channel ->
            ChannelRow(channel, programMap[channel] ?: emptyList())
        }
    }
}

@Composable
fun ChannelRow(channel: PreviewChannel, programs: List<PreviewProgram>) {
    if (programs.isEmpty()) return
    val context = LocalContext.current

    Column {
        Text(
            text = channel.displayName.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            programs.forEach { program ->
                Column {
                    Card(shape = AppTheme.cardShape,
                        modifier = Modifier.clickable {
                            val intent = program.intent
                            if (intent != null) {
                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Image(
                            painter = rememberImagePainter(data = program.thumbnailUri),
                            contentDescription = "Thumbnail for for ${program.title}",
                            modifier = Modifier.size(128.dp)
                        )
                    }
                    Text(
                        text = program.title ?: "empty",
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

@Preview(uiMode = Configuration.UI_MODE_TYPE_TELEVISION)
@Composable
fun PreviewChannel() {
    ChannelsScreenContent(emptyMap())
}