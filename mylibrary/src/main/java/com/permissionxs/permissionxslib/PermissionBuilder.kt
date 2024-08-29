package com.permissionxs.permissionxslib

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.content.pm.ConfigurationInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Message
import android.service.voice.VoiceInteractionSession.ActivityId
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.permissionxs.permissionxslib.callback.RequestCallback
import com.permissionxs.permissionxslib.callback.ForwardToSettingsCallback
import com.permissionxs.permissionxslib.dialog.DefaultDialog
import com.permissionxs.permissionxslib.dialog.RationaleDialog

/**
 * providing more APIs for developers to control PermissionXS functions
 */

class PermissionBuilder(
    fragmentActivity: FragmentActivity?,
    fragment: Fragment?,
    normalPermissions: MutableSet<String>,
    specialPermissions: MutableSet<String>
) {

    lateinit var activity: FragmentActivity
    private var fragment:Fragment? = null
    private var originRequestOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    private val fragmentManager: FragmentManager
        get() {
            return fragment?.childFragmentManager ?: activity.supportFragmentManager
        }

    private val invisibleFragment: InvisibleFragment
        get() {
            val existedFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG)
            return if (existedFragment != null) {
                existedFragment as InvisibleFragment
            } else {
                val invisibleFragment = InvisibleFragment()
                fragmentManager.beginTransaction()
                    .add(invisibleFragment, FRAGMENT_TAG)
                    .commitNowAllowingStateLoss()
                invisibleFragment
            }
        }

    @JvmField
    var currentDialog: Dialog? = null

    @JvmField
    var normalPermissions: MutableSet<String>

    @JvmField
    var specialPermissions: MutableSet<String>

    @JvmField
    var showDialogCalled = false

    @JvmField
    var permissionsWontRequest: MutableSet<String> = LinkedHashSet()

    @JvmField
    var grantedPermissions: MutableSet<String> = LinkedHashSet()

    @JvmField
    var deniedPermissions: MutableSet<String> = LinkedHashSet()

    @JvmField
    var permanentDeniedPermissions: MutableSet<String> = LinkedHashSet()

    @JvmField
    var forwardPermissions: MutableSet<String> = LinkedHashSet()

    @JvmField
    var requestCallback: RequestCallback? = null

    @JvmField
    var forwardToSettingsCallback: ForwardToSettingsCallback? = null

    val targetSdkVersion: Int
        get() = activity.applicationInfo.targetSdkVersion

    fun onForwardToSettings(callback: ForwardToSettingsCallback): PermissionBuilder {
        forwardToSettingsCallback = callback
        return this
    }

    fun request(callback: RequestCallback) {
        requestCallback = callback
        startRequest()
    }

    fun showHandlePermissionDialog(
        chainTask: ChainTask,
        showReasonOrGoSettings: Boolean,
        permissions: List<String>,
        message: String,
        positiveText: String,
        negativeText: String?
    ) {
        val defaultDialog = DefaultDialog(
            activity,
            permissions,
            message,
            positiveText,
            negativeText.toString()
        )
        showHandlePermissionDialog(chainTask, showReasonOrGoSettings, defaultDialog)
    }

    fun showHandlePermissionDialog(
        chainTask: ChainTask,
        showReasonOrGoSettings: Boolean,
        dialog: RationaleDialog
    ) {
        showDialogCalled = true
        val permissions = dialog.permissionsToRequest
        if (permissions.isEmpty()) {
            chainTask.finish()
            return
        }
        currentDialog = dialog
        dialog.show()

    }

    private fun startRequest() {
        lockOrientation()
        val requestChain = RequestChain()
    }

    private fun lockOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
            originRequestOrientation = activity.requestedOrientation
            val orientation = activity.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            }
        }
    }

    companion object {
        private const val FRAGMENT_TAG = "InvisibleFragment"

    }

    init {
        if (fragmentActivity != null) {
            activity = fragmentActivity
        }
        if (fragmentActivity == null && fragment != null) {
            activity = fragment.requireActivity()
        }
        this.fragment = fragment
        this.normalPermissions = normalPermissions
        this.specialPermissions = specialPermissions
    }

}