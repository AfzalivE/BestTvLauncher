package com.afzaln.besttvlauncher.utils

import android.app.StatusBarManager

/**
 * https://stackoverflow.com/a/27053173/507142
 */
fun StatusBarManager.expand() {
    val statusBarManager = Class.forName("android.app.StatusBarManager")
    val expandMethod = statusBarManager.getMethod("expandNotificationsPanel")
    expandMethod.invoke(this)
}
