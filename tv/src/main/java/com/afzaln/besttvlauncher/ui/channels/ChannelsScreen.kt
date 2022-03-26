package com.afzaln.besttvlauncher.ui.channels

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import androidx.tvprovider.media.tv.BasePreviewProgram
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.WatchNextProgram
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.afzaln.besttvlauncher.ui.ItemDetails
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.*
import kotlinx.coroutines.launch

@Composable
fun ChannelsScreen() {
    val viewModel: HomeViewModel = locatorViewModel()
    val programList by viewModel.programsByChannel.observeAsState(emptyMap())
    val watchNextList by viewModel.watchNextChannel.observeAsState(emptyList())
    val materialBackgroundColor = MaterialTheme.colorScheme.background
    val backgroundColor by viewModel.backgroundColor.observeAsState(materialBackgroundColor)

    LaunchedEffect(key1 = Unit, block = {
        viewModel.loadData()
    })

    val navigator = LocalNavigator.currentOrThrow.parent!!

    ChannelsScreenContent(
        programList = programList,
        watchNextList = watchNextList,
        backgroundColor = backgroundColor,
        onCardFocus = { palette ->
            viewModel.palette.value = palette
        }
    ) { channelId, programId ->
        navigator.push(ItemDetails(channelId = channelId, programId = programId))
    }
}

@Composable
private fun ChannelsScreenContent(
    programList: Map<PreviewChannel, List<PreviewProgram>>,
    watchNextList: List<WatchNextProgram>,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onCardFocus: (Palette) -> Unit,
    onProgramClicked: (Long, Long) -> Unit
) {
    Column(
        modifier = Modifier
            .background(backgroundColor)
            .padding(horizontal = 48.dp)
            .padding(top = 27.dp, bottom = 27.dp)
    ) {
        ChannelList(programList, watchNextList, onCardFocus, onProgramClicked)
    }
}

@Composable
fun ChannelList(
    programMap: Map<PreviewChannel, List<PreviewProgram>>,
    watchNextList: List<WatchNextProgram>,
    onCardFocus: (Palette) -> Unit,
    onProgramClicked: (Long, Long) -> Unit
) {
    val channels = programMap.keys.toList()
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        channels.forEach { channel ->
            ProgramInChannelRow(
                channel = channel,
                programs = programMap[channel] ?: emptyList(),
                onCardFocus = onCardFocus,
                onProgramClicked = onProgramClicked
            )
        }
        WatchNextRow(watchNextList, onCardFocus)
    }
}

@Composable
fun WatchNextRow(programs: List<WatchNextProgram>, onCardFocus: (Palette) -> Unit) {
    CardRow(
        title = "Watch Next",
        programs = programs,
        onClick = {},
        onCardFocus = onCardFocus
    )
}

@Composable
fun ProgramInChannelRow(
    channel: PreviewChannel,
    programs: List<PreviewProgram>,
    onCardFocus: (Palette) -> Unit,
    onProgramClicked: (Long, Long) -> Unit
) {
    if (programs.isEmpty()) return

    val context = LocalContext.current

    CardRow(
        title = channel.displayName.toString(),
        programs = programs,
        onClick = { programId ->
            onProgramClicked(channel.id, programId)
            // TODO: Handle Channels where intent should launch directly.
            // val intent = program.intent
            // if (intent != null) {
            //     context.startActivity(intent)
            // }
        },
        onCardFocus = onCardFocus
    )
}

@Composable
fun CardRow(
    title: String,
    programs: List<BasePreviewProgram>,
    onClick: (programId: Long) -> Unit,
    onCardFocus: (Palette) -> Unit
) {
    if (programs.isEmpty()) return

    val context = LocalContext.current

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            programs.forEach { program ->
                ProgramCard(
                    program,
                    onFocus = onCardFocus,
                    onClick = { onClick(program.id) },
                )
            }
        }
    }
}

@Composable
private fun ProgramCard(
    program: BasePreviewProgram,
    onFocus: (Palette) -> Unit,
    onClick: () -> Unit,
) {
    var isClicked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var palette by remember { mutableStateOf(emptyPalette()) }

    val animatedScale by animateFloatAsState(
        animationSpec = tween(1000),
        targetValue = if (isClicked) 1.25f else 1f,
        finishedListener = {
            onClick()
        }
    )

    Column(
        modifier = Modifier
            .scale(animatedScale)
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = AppTheme.cardShape,
            modifier = Modifier
                .requiredHeight(120.dp)
                .aspectRatio(program.posterAspectRatio())
                .dpadFocusable(
                    shadowColor = Color(palette.vibrantSwatch?.rgb ?: 0),
                    unfocusedBorderColor = MaterialTheme.colorScheme.background,
                    onFocus = {
                        if (it.isFocused) {
                            onFocus(palette)
                        }
                    }
                )
                .clickable {
                    isClicked = true
                }
        ) {
            AsyncImage(
                model = program.posterArtUri,
                contentDescription = "Thumbnail for ${program.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                onState = { state ->
                    if (state is AsyncImagePainter.State.Success) {
                        coroutineScope.launch {
                            palette = createPalette(state.result.drawable)
                        }
                    }
                }
            )
        }
        Text(
            text = program.title ?: "empty",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.widthIn(max = 160.dp)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_TYPE_TELEVISION)
@Composable
fun PreviewChannel() {
    ChannelsScreenContent(
        emptyMap(),
        emptyList(),
        onCardFocus = {}
    ) { _, _ -> }
}
