package com.afzaln.besttvlauncher.ui.itemdetails

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.afzaln.besttvlauncher.R
import com.afzaln.besttvlauncher.data.models.Program
import com.afzaln.besttvlauncher.ui.apps.HomeViewModel
import com.afzaln.besttvlauncher.ui.theme.Gray20
import com.afzaln.besttvlauncher.ui.theme.Gray700
import com.afzaln.besttvlauncher.utils.locatorViewModel
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ItemDetailsScreen(channelId: Long, programId: Long) {
    val viewModel: HomeViewModel = locatorViewModel()
    val programMap by viewModel.programsByChannel.observeAsState(emptyMap())

    AnimatedVisibility(
        visible = programMap.isNotEmpty(),
        enter = fadeIn(tween()),
        exit = fadeOut(tween())
    ) {
        val channel = programMap.keys.first { it.id == channelId }
        val program = requireNotNull(programMap[channel]).first { it.id == programId }

        ItemDetailsContent(program = program)
    }
}

@Composable
fun ItemDetailsContent(program: Program) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout {
            val (header, titleDetails) = createRefs()

            MediaHeaderImage(
                program.posterArtUri.toUri(),
                modifier = Modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                }
            )
            ItemInfo(titleDetails, program)
        }
    }
}

@Composable
private fun ConstraintLayoutScope.ItemInfo(
    titleDetails: ConstrainedLayoutReference,
    program: Program
) {

    val initialState = AnimationState(opacity = 0f, scale = 0.75f)
    val finalState = AnimationState(opacity = 1f, scale = 1f)

    val state = remember { MutableTransitionState(initialState) }.apply {
        // Start the animation immediately.
        targetState = finalState
    }

    val transition = updateTransition(
        transitionState = state,
        label = "ItemInfo"
    )

    val animationState = transition.animateContent()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .constrainAs(titleDetails) {
                bottom.linkTo(parent.bottom)
            }
            .padding(bottom = 48.dp)
            .padding(horizontal = 64.dp)
            .graphicsLayer(
                transformOrigin = TransformOrigin(0f, 1f),
                scaleX = animationState.scale, scaleY = animationState.scale
            )
            .alpha(animationState.opacity)
    ) {
        Text(
            text = program.title ?: "Empty title",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        MetadataRow(program)
        program.description?.let { description ->
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                modifier = Modifier.fillMaxWidth(0.75f)
            )
        }
    }
    ButtonRow(program)
}

data class AnimationState(
    val blur: Dp = 0.dp,
    val opacity: Float,
    val scale: Float
)

@Composable
fun MediaHeaderImage(
    posterArtUri: Uri,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val initialState = AnimationState(blur = 1.dp, opacity = 0f, scale = 1.25f)
    val finalState = AnimationState(blur = 0.dp, opacity = 1f, scale = 1f)

    val state = remember { MutableTransitionState(initialState) }.apply {
        // Start the animation immediately.
        targetState = finalState
    }

    val transition = updateTransition(
        transitionState = state,
        label = "MediaHeader"
    )

    val animationState = transition.animateContent()

    AsyncImage(
        model = posterArtUri,
        contentDescription = stringResource(id = R.string.content_description_poster_art),
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .scale(animationState.scale)
            .blur(animationState.blur)
            .alpha(animationState.opacity)
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
private fun Transition<AnimationState>.animateContent(): AnimationState {
    val blur by animateDp(label = "Blur",
        transitionSpec = {
            tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        }
    ) { it.blur }

    val opacity by animateFloat(
        label = "Opacity",
        transitionSpec = {
            tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        }
    ) { it.opacity }

    val scale by animateFloat(
        label = "Opacity",
        transitionSpec = {
            tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        }
    ) { it.scale }
    return AnimationState(blur, opacity, scale)
}

@Composable
fun MetadataRow(program: Program) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        program.genre?.let {
            Text(
                text = program.genre,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
        }

        program.releaseDate?.let { releaseDate ->
            Text(
                text = releaseDate,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
        }

        val durations =
            program.durationMillis.milliseconds.toComponents { hours, minutes, _, _ ->
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
fun ButtonRow(program: Program) {
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
