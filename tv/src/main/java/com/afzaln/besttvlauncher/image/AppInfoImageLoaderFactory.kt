package com.afzaln.besttvlauncher.image

import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory

class AppInfoImageLoaderFactory(val context: Context) : ImageLoaderFactory {
    fun init() {
        Coil.setImageLoader(this)
    }

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(context)
        .components {
            add(AppBannerFetcher.Factory(context))
        }
        .build()
}
