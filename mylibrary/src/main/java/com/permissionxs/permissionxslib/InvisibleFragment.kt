package com.permissionxs.permissionxslib

import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class InvisibleFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())

    private fun postForResult(callback: () -> Unit) {
        handler.post {
            callback()
        }
    }

    val requestNormalPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { grantResult ->
        postForResult {
            onRequestNormalPermissionsResult()
        }
    }


    fun requestNow(permissions: Set<String>) {
        requestNormalPermissionLauncher.launch(permissions.toTypedArray())
    }
}