package com.github.sdp.tarjetakuna.utils

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

class PermissionGranting {
    object PermissionGranting {
        fun grantPermission() {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            if (Build.VERSION.SDK_INT >= 23) {
                val allowPermission = UiDevice.getInstance(instrumentation).findObject(
                    UiSelector().text(
                        when {
                            Build.VERSION.SDK_INT == 23 -> "Allow"
                            Build.VERSION.SDK_INT <= 28 -> "ALLOW"
                            Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
                            else -> "While using the app"
                        }
                    )
                )
                if (allowPermission.exists()) {
                    allowPermission.click()
                }
            }
        }
    }
}
