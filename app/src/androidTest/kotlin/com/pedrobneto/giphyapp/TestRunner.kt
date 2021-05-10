package com.pedrobneto.giphyapp

import android.os.Bundle
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner
import androidx.test.uiautomator.UiDevice

class TestRunner : AndroidJUnitRunner() {
    override fun onStart() {
        super.onStart()
        setupDeviceConfigurations(true)
    }

    override fun finish(resultCode: Int, results: Bundle?) {
        setupDeviceConfigurations(false)
        super.finish(resultCode, results)
    }

    private fun setupDeviceConfigurations(isTestConfiguration: Boolean) {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val scale = if (isTestConfiguration) 0.0 else 1.0
        uiDevice.executeShellCommand(" settings put global window_animation_scale $scale")
        uiDevice.executeShellCommand(" settings put global transition_animation_scale $scale")
        uiDevice.executeShellCommand(" settings put global animator_duration_scale $scale")
    }
}