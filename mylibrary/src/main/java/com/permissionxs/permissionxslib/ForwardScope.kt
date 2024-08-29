package com.permissionxs.permissionxslib

class ForwardScope internal constructor(
    private val pb: PermissionBuilder,
    private val chainTask: ChainTask
){
    @JvmOverloads
    fun showForwardToSettingsDialog(permissions: List<String>, message: String, positiveText: String, negativeText:String? = null) {
        pb.showHandlePermissionDialog(chainTask, false, permissions, message, positiveText, negativeText)
    }
}