package com.afzaln.besttvlauncher.utils

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.afzaln.besttvlauncher.ui.theme.AppTheme

fun Modifier.dpadFocusable(
    unfocusedBorderWidth: Dp = 0.dp,
    focusedBorderWidth: Dp = 2.dp,
    unfocusedBorderColor: Color = Color.Black,
    focusedBorderColor: Color = Color.White,
    shadowColor: Color = Color.Black,
    onFocus: (FocusState) -> Unit
) = composed {
    val boxInteractionSource = remember { MutableInteractionSource() }
    val isItemFocused by boxInteractionSource.collectIsFocusedAsState()

    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isItemFocused) focusedBorderWidth else unfocusedBorderWidth
    )

    val infiniteTransition = rememberInfiniteTransition()
    val animatedFocusedBorder by infiniteTransition.animateColor(
        initialValue = focusedBorderColor.copy(alpha = 0.5f),
        targetValue = focusedBorderColor,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isItemFocused) animatedFocusedBorder else unfocusedBorderColor
    )

    val animatedShadowColor by animateColorAsState(
        targetValue = if (isItemFocused) shadowColor else unfocusedBorderColor
    )

    val animatedShadowElevation by animateDpAsState(
        targetValue = if (isItemFocused) 48.dp else 0.dp
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isItemFocused) 1.1f else 1f
    )

    this
        .onFocusChanged { onFocus(it) }
        .focusable(interactionSource = boxInteractionSource)
        .scale(animatedScale)
        .shadow(animatedShadowElevation, ambientColor = animatedShadowColor)
        .border(
            width = animatedBorderWidth,
            color = animatedBorderColor,
            shape = AppTheme.cardShape
        )
}
