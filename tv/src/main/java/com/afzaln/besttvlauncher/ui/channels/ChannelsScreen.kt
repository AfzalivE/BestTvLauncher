package com.afzaln.besttvlauncher.ui.channels

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import coil.compose.rememberImagePainter
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

    Column {
        Text(
            text = channel.displayName.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            programs.forEach { program ->
                ProgramCard(program, onFocus = {})
            }
        }
    }
}

@Composable
private fun ProgramCard(
    program: PreviewProgram,
    onFocus: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = AppTheme.cardShape,
            modifier = Modifier
                .requiredHeight(120.dp)
                .requiredWidth(160.dp)
                .dpadFocusable(
                    unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    onFocus = onFocus
                )
                .clickable {
                    val intent = program.intent
                    if (intent != null) {
                        context.startActivity(intent)
                    }
                }
        ) {
            Image(
                painter = rememberImagePainter(data = program.thumbnailUri),
                contentDescription = "Thumbnail for for ${program.title}",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = program.title?.take(20) ?: "empty",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_TYPE_TELEVISION)
@Composable
fun PreviewChannel() {
    ChannelsScreenContent(emptyMap())
}
