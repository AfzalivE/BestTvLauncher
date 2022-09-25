package com.afzaln.besttvlauncher.utils

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun emptyPalette() = Palette.from(listOf(Palette.Swatch(0, 0)))

suspend fun createPalette(drawable: Drawable): Palette {
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        drawable.toBitmap().asShared()
    } else {
        drawable.toBitmap()
//        TODO("VERSION.SDK_INT < S")
    }
    return withContext(Dispatchers.Default) {
        Palette.from(bitmap).generate()
    }
}
