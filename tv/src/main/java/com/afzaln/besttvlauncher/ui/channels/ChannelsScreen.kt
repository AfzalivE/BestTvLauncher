package com.afzaln.besttvlauncher.ui.channels

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.afzaln.besttvlauncher.data.models.Channel
import com.afzaln.besttvlauncher.data.models.Program
import com.afzaln.besttvlauncher.data.models.posterAspectRatio
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.theme.AppTheme
import com.afzaln.besttvlauncher.utils.createPalette
import com.afzaln.besttvlauncher.utils.dpadFocusable
import com.afzaln.besttvlauncher.utils.emptyPalette
import com.afzaln.besttvlauncher.utils.recomposeHighlighter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChannelsScreen(
    state: HomeViewModel.State,
    onProgramClicked: (Long, Long) -> Unit,
    onCardFocus: (Palette) -> Unit
) {
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
                    onCardFocus = onCardFocus,
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
    programList: ImmutableMap<Channel, ImmutableList<Program>>,
    watchNextList: ImmutableList<Program>,
    onCardFocus: (Palette) -> Unit,
    onProgramClicked: (Long, Long) -> Unit
) {
    Column {
        ChannelList(programList, watchNextList, onCardFocus, onProgramClicked)
    }
}

@Composable
fun ChannelList(
    programMap: ImmutableMap<Channel, ImmutableList<Program>>,
    watchNextList: ImmutableList<Program>,
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
                programs = programMap[channel] ?: persistentListOf(),
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
fun WatchNextRow(programs: ImmutableList<Program>, onCardFocus: (Palette) -> Unit) {
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
    channel: Channel,
    programs: ImmutableList<Program>,
    onCardFocus: (Palette) -> Unit,
    onProgramClicked: (Long, Long) -> Unit
) {
    if (programs.isEmpty()) return

    val context = LocalContext.current

    CardRow(
        title = channel.displayName,
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
    programs: ImmutableList<Program>,
    onClick: (programId: Long) -> Unit,
    onCardFocus: (Palette) -> Unit
) {
    if (programs.isEmpty()) return
    var hasFocus by remember { mutableStateOf(false) }

    val animatedTitleSize by animateIntAsState(targetValue = if (hasFocus) 24 else 14)

    val context = LocalContext.current

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = animatedTitleSize.sp),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(contentPadding)
        )
        TvLazyRow(
            modifier = Modifier.onFocusChanged {
                hasFocus = it.hasFocus
            },
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
    program: Program,
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

    val height = 160
    val width = height * program.posterAspectRatio()

    Column(
        modifier = Modifier
            .widthIn(max = width.dp)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .scale(animatedScale)
            .recomposeHighlighter(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = AppTheme.cardShape,
            modifier = Modifier
                .heightIn(max = height.dp)
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
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(top = 8.dp),
            maxLines = 2,
            overflow = TextOverflow.Visible
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_TYPE_TELEVISION)
@Composable
fun Channel() {
    ChannelsScreenContent(
        persistentMapOf(),
        persistentListOf(),
        onCardFocus = {}
    ) { _, _ -> }
}
