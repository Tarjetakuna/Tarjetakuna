package com.github.sdp.tarjetakuna.utils

import android.Manifest
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry

class PermissionGranting {
    object PermissionGranting {
        // Permissions required for the app to run
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        fun grantPermissions() {
            REQUIRED_PERMISSIONS.forEach {
                grantPermission(it)
            }
        }

        private fun grantPermission(permission: String) {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            instrumentation.uiAutomation.grantRuntimePermission(
                instrumentation.targetContext.packageName,
                permission
            )
        }
    }
}
