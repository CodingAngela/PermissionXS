package com.permissionxs.permissionxslib

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionxs.permissionxslib.allSpecialPermissions

class PermissionMediator {

    private var activity: FragmentActivity? = null
    private var fragment: Fragment? = null

    constructor(activity: FragmentActivity) {
        this.activity = activity
    }

    constructor(fragment: Fragment) {
        this.fragment =fragment
    }

    // permissions that will be requested
    fun permissions(permissions: List<String>): PermissionBuilder {
        val normalPermissionSet = LinkedHashSet<String>()
        val specialPermissionSet = LinkedHashSet<String>()
        val osVerion = Build.VERSION.SDK_INT
        val targetSdkVersion = if (activity != null) {
            activity!!.applicationInfo.targetSdkVersion
        } else {
            fragment!!.requireContext().applicationInfo.targetSdkVersion
        }
        for (permission in permissions) {
            if (permission in allSpecialPermissions){
                specialPermissionSet.add(permission)
            } else {
                normalPermissionSet.add(permission)
            }
        }
        if ("android.permission.ACCESS_BACKGROUND_LOCATION" in specialPermissionSet) {
            if (osVerion == Build.VERSION_CODES.Q || (osVerion == Build.VERSION_CODES.R && targetSdkVersion < Build.VERSION_CODES.R)) {
                // if we request AccessBackgroundLocation on Q or R but targetSdkVersion below R, we don't need to rquest specially, just request as normal permission
                specialPermissionSet.remove("android.permission.ACCESS_BACKGROUND_LOCATION")
                normalPermissionSet.add("android.permission.ACCESS_BACKGROUND_LOCATION")
            }
        }
        if ("android.permission.POST_NOTIFICATIONS" in specialPermissionSet) {
            if (osVerion >= Build.VERSION_CODES.TIRAMISU && targetSdkVersion >= Build.VERSION_CODES.TIRAMISU) {
                // if we request POST_NOTIFICATIONS on TIRAMISU or above and targetSdkVersion >= TIRAMISU, we don't need request specially, just request as normal permission
                specialPermissionSet.remove("android.permission.POST_NOTIFICATIONS")
                normalPermissionSet.add("android.permission.POST_NOTIFICATIONS")
            }
        }
        return PermissionBuilder(activity, fragment, normalPermissionSet, specialPermissionSet)
    }

    fun permissions(vararg permissions: String): PermissionBuilder {
        return permissions(listOf(*permissions))
    }

}