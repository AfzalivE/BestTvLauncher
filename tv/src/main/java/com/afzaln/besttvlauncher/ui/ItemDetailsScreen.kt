package com.afzaln.besttvlauncher.ui

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.tvprovider.media.tv.BasePreviewProgram
import androidx.tvprovider.media.tv.PreviewProgram
import coil.compose.rememberImagePainter
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.theme.Gray20
import com.afzaln.besttvlauncher.ui.theme.Gray700
import com.afzaln.besttvlauncher.utils.locatorViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlin.time.Duration.Companion.milliseconds

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
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
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
                MetadataRow(program)
                Text(
                    text = program.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                    modifier = Modifier.fillMaxWidth(0.75f)
                )
                ButtonRow(program)
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

@Composable
fun MetadataRow(previewProgram: BasePreviewProgram) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = previewProgram.genre,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )

        Text(
            text = previewProgram.releaseDate,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )

        val durations =
            previewProgram.durationMillis.milliseconds.toComponents { hours, minutes, _, _ ->
                arrayOf(hours, minutes)
            }

        Text(
            text = stringResource(id = R.string.duration, formatArgs = durations),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
        )
    }
}

@Composable
fun ButtonRow(previewProgram: BasePreviewProgram) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val durationMillis = 150
    val animatedScale by animateFloatAsState(
        animationSpec = tween(durationMillis),
        targetValue = if (isFocused) 1.1f else 1f
    )

    val animatedBackground by animateColorAsState(
        animationSpec = tween(durationMillis),
        targetValue = if (isFocused) {
            Color.White
        } else {
            Gray700.copy(alpha = 0.50f)
        }
    )

    val animatedTextColor by animateColorAsState(
        animationSpec = tween(durationMillis),
        targetValue = if (isFocused) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    )


    Button(
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedBackground,
            contentColor = animatedTextColor
        ),
        modifier = Modifier
            .scale(animatedScale),
        onClick = {},
        interactionSource = interactionSource
    ) {
        Text(
            text = "Watch now",
            color = animatedTextColor
        )
    }
}
