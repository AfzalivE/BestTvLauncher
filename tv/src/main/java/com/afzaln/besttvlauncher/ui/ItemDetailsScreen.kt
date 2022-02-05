package com.afzaln.besttvlauncher.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.tvprovider.media.tv.PreviewProgram
import coil.compose.rememberImagePainter
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.theme.Gray20
import com.afzaln.besttvlauncher.utils.locatorViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ItemDetailsScreen(channelId: Long, programId: Long) {
    val viewModel: HomeViewModel = locatorViewModel()
    val programMap by viewModel.programsByChannel.observeAsState(emptyMap())

    if (programMap.isNotEmpty()) {
        val channel = programMap.keys.first { it.id == channelId }
        val program = requireNotNull(programMap[channel]).first { it.id == programId }
        ItemDetailsContent(program = program)
    }
}

@Composable
fun ItemDetailsContent(program: PreviewProgram) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout {
            val (header, titleDetails) = createRefs()

            MediaHeaderImage(
                program.posterArtUri,
                modifier = Modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                }
            )
            Column(modifier = Modifier
                .constrainAs(titleDetails) {
                    bottom.linkTo(header.bottom)
                }
                .padding(bottom = 48.dp)
                .padding(horizontal = 64.dp)
            ) {
                Text(
                    text = program.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = program.description,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun MediaHeaderImage(
    posterArtUri: Uri,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Image(
        painter = rememberImagePainter(data = posterArtUri),
        contentDescription = stringResource(id = R.string.content_description_poster_art),
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .fillMaxWidth()
            .drawWithCache {
                val gradient = Brush.horizontalGradient(
                    colors = listOf(Gray20, Color.Gray),
                    startX = 0f,
                    endX = size.width
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.Multiply)
                }
            }
            .requiredHeight(screenHeight.dp)
    )
}
