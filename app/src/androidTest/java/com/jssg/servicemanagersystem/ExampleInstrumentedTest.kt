package com.jssg.servicemanagersystem

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jssg.servicemanagersystem.utils.download.UpdateEntity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.jssg.servicemanagersystem", appContext.packageName)
    }
    @Test
    fun getVersionCode() {
        val versionCode = "120".toCharArray().joinToString(".")
        assertEquals("1.2.0", versionCode)
    }
}