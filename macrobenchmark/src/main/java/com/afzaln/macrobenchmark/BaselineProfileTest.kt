package com.afzaln.macrobenchmark

import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Ignore
import org.junit.Test

private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

class BaselineProfileTest {

    @Test
    @Ignore("Use only for writing the benchmark test.")
    fun runStandaloneBenchmarkTest() {
        startApp()
        benchmarkTest()
    }
}

fun benchmarkTest() {
    allowPermissionsIfNeeded()

    device.pressKeyCode(KeyEvent.KEYCODE_DPAD_RIGHT)
    device.pressKeyCode(KeyEvent.KEYCODE_DPAD_RIGHT)
    device.pressKeyCode(KeyEvent.KEYCODE_DPAD_RIGHT)
    // Just a way to wait for 2 seconds, this object doesn't exist.
    device.wait(Until.hasObject(By.text("Recommended").depth(0)), 1000)
    device.pressKeyCode(KeyEvent.KEYCODE_DPAD_LEFT)
    device.wait(Until.hasObject(By.text("Recommended").depth(0)), 1000)
    repeat(5) {
        device.pressKeyCode(KeyEvent.KEYCODE_DPAD_DOWN)
    }
}


private fun startApp() {
    val targetPackage = "com.afzaln.besttvlauncher"
    val context = InstrumentationRegistry.getInstrumentation().context
    val intent = context.packageManager.getLaunchIntentForPackage(targetPackage)
    intent ?: throw IllegalStateException("Unable to acquire intent for package $targetPackage")
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
    device.wait(Until.hasObject(By.pkg(targetPackage).depth(0)), 5000)
}

private fun allowPermissionsIfNeeded() {
    val allowPermissions = device.findObject(UiSelector().text("Allow"))
    if (allowPermissions.exists()) {
        try {
            allowPermissions.click()
        } catch (e: UiObjectNotFoundException) {
            Log.e("ColdStartupBenchmark", "There is no permissions dialog to interact with ", e)
        }
    }
}
