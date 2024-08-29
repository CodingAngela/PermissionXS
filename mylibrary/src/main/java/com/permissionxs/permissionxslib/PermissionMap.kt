package com.permissionxs.permissionxslib

import android.Manifest


// maintains all special permissions that we need to handle by special case
val allSpecialPermissions = setOf(
//    RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION,
    "android.permission.ACCESS_BACKGROUND_LOCATION",
    "android.permission.MANAGE_EXTERNAL_STORAGE",
    "android.permission.REQUEST_INSTALL_PACKAGES",
    "android.permission.POST_NOTIFICATIONS",
    "android.permission.BODY_SENSORS_BACKGROUND",
    Manifest.permission.SYSTEM_ALERT_WINDOW,
    Manifest.permission.WRITE_SETTINGS
)