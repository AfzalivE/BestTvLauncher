package com.afzaln.besttvlauncher.utils

import android.annotation.SuppressLint
import androidx.tvprovider.media.tv.BasePreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat

@SuppressLint("RestrictedApi")
fun BasePreviewProgram.posterAspectRatio(): Float = when (posterArtAspectRatio) {
    // TODO publish AndroidTvProvider fork 1.0.2
    TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_16_9 -> 16f / 9
    TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_3_2 -> 3f / 2
    TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_4_3 -> 4f / 3
    TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_1_1 -> 1f
    TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_2_3 -> 2f / 3
    TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_MOVIE_POSTER -> 1 / 1.441f
    TvContractCompat.PreviewProgramColumns.ASPECT_RATIO_3_4 -> 3f / 4
    else -> 4f / 3
}
