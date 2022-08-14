package com.afzaln.besttvlauncher.ui.channels

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tvprovider.media.tv.BasePreviewProgram
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.WatchNextProgram
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.dpadFocusable
import com.afzaln.besttvlauncher.utils.emptyPalette
import com.afzaln.besttvlauncher.utils.posterAspectRatio

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChannelsScreen(state: HomeViewModel.State, onProgramClicked: (Long, Long) -> Unit) {
    val materialBackgroundColor = MaterialTheme.colorScheme.background
//    val backgroundColor by viewModel.backgroundColor.observeAsState(materialBackgroundColor)

    AnimatedContent(targetState = state, transitionSpec = {
        fadeIn(tween(250)) with fadeOut(tween(250))
    }) { targetState ->
        when (targetState) {
            is HomeViewModel.State.Loaded -> {
                ChannelsScreenContent(
                    programList = targetState.programsByChannel,
                    watchNextList = targetState.watchNextPrograms,
                    backgroundColor = materialBackgroundColor,
                    onCardFocus = { palette ->
//                        viewModel.palette.value = palette
                    },
                    onProgramClicked = onProgramClicked
                )
            }
            HomeViewModel.State.Loading -> {
                LoadingChannelScreen()
            }
        }
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
    TvLazyColumn(
        contentPadding = PaddingValues(vertical = 27.dp),
        pivotOffsets = PivotOffsets(
            parentFraction = 0.15f,
        )
    ) {
        items(channels) { channel ->
            ProgramInChannelRow(
                channel = channel,
                programs = programMap[channel] ?: emptyList(),
                onCardFocus = onCardFocus,
                onProgramClicked = onProgramClicked
            )
        }
        item {
            WatchNextRow(watchNextList, onCardFocus)
        }
    }
}

@Composable
fun WatchNextRow(programs: List<WatchNextProgram>, onCardFocus: (Palette) -> Unit) {
    CardRow(
        title = "Watch Next",
        programs = programs,
        onClick = {},
        onCardFocus = onCardFocus,
        contentPadding = PaddingValues(horizontal = 48.dp)
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
        onCardFocus = onCardFocus,
        contentPadding = PaddingValues(horizontal = 48.dp)
    )
}

@Composable
fun CardRow(
    title: String,
    contentPadding: PaddingValues,
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
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(contentPadding)
        )
        TvLazyRow(
            contentPadding = contentPadding,
            pivotOffsets = PivotOffsets(
                parentFraction = 0.05f,
            )
        ) {
            items(programs) { program ->
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
//                        coroutineScope.launch {
//                            palette = createPalette(state.result.drawable)
//                        }
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
