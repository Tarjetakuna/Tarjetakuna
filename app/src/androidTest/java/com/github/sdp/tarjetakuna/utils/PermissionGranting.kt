package com.github.sdp.tarjetakuna.utils

import android.Manifest
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry

/**
 * Helper to grant permissions to the app to run the tests
 */
class PermissionGranting {
    object PermissionGranting {
        /**
         * Permissions required for the app to run
         */
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        /**
         * Grant all permissions required for the app to run
         */
        fun grantPermissions() {
            REQUIRED_PERMISSIONS.forEach {
                grantPermission(it)
            }
        }

        /**
         * Grant a single permission
         */
        private fun grantPermission(permission: String) {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            instrumentation.uiAutomation.grantRuntimePermission(
                instrumentation.targetContext.packageName,
                permission
            )
        }
    }
}
