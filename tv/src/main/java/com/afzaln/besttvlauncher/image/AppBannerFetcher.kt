package com.afzaln.besttvlauncher.image

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.request.Options
import com.afzaln.besttvlauncher.data.models.AppInfo

private const val SCHEME_APPINFO = "appinfo"

class AppBannerFetcher(val context: Context, val data: Uri) : Fetcher {
    override suspend fun fetch(): FetchResult? {
        if (data.scheme != SCHEME_APPINFO) return null

        val packageName = data.host ?: return null
        val activityName = data.lastPathSegment ?: return null
        val packageManager = context.packageManager

        val drawable = packageManager.getActivityBanner(ComponentName.createRelative(packageName, activityName))
        drawable ?: return null

        return DrawableResult(drawable, false, DataSource.DISK)
    }

    class Factory(private val context: Context) : Fetcher.Factory<Uri> {
        override fun create(data: Uri, options: Options, imageLoader: ImageLoader): Fetcher =
            AppBannerFetcher(context, data)
    }
}

fun AppInfo.toPackageUri() = "$SCHEME_APPINFO://$packageName/$activityName"
