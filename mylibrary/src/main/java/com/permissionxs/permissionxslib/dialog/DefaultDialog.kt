package com.permissionxs.permissionxslib.dialog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import com.permissionxs.app.Manifest
import com.permissionxs.permissionxslib.R
import com.permissionxs.permissionxslib.allSpecialPermissions
import com.permissionxs.permissionxslib.databinding.PermissionxsDefaultDialogLayoutBinding
import com.permissionxs.permissionxslib.databinding.PermissionxsPermissionItemBinding
import kotlinx.coroutines.MainScope

/**
 * Default rationale dialog to show if developers did not implement thier own custom rationale dialog
 */
class DefaultDialog(context: Context,
    private val permissions: List<String>,
    private val message: String,
    private val positiveText: String,
    private val negetiveText: String
) : RationaleDialog(context, R.style.PermissionXSDefaultDialog) {

    private lateinit var binding: PermissionxsDefaultDialogLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PermissionxsDefaultDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupText()
        buildPermissionLayout()
        setupWindow()
    }

    private fun setupText() {
        binding.messageText.text = message
        binding.positiveBtn.text = positiveText
        if (negetiveText != null) {
            binding.negativeLayout.visibility = View.VISIBLE
            binding.negativeBtn.text = negetiveText
        } else {
            binding.negativeLayout.visibility = View.GONE
        }
    }

    /**
     * Add every permission that need to explain the request reason to the dialog
     */
    private fun buildPermissionLayout() {
        val tempSet = HashSet<String>()
        val currentVersion = Build.VERSION.SDK_INT
        for (permission in permissions) {
            val permissionGroup = when {
                currentVersion < Build.VERSION_CODES.Q -> {
                    try {
                        val permissionInfo = context.packageManager.getPermissionInfo(permission, 0)
                        permissionInfo.group
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                        null
                    }
                }
                currentVersion == Build.VERSION_CODES.Q -> permissionMapOnQ[permission]
                currentVersion == Build.VERSION_CODES.R -> premissionMapOnR[permission]
                currentVersion == Build.VERSION_CODES.S -> permissionMapOnS[permission]
                currentVersion == Build.VERSION_CODES.TIRAMISU -> permissionMapOnT[permission]
                else -> {
                    permissionMapOnT[permission]
                }
            }
            if ((permission in allSpecialPermissions && !tempSet.contains(permission)) ||
                (permissionGroup != null && !tempSet.contains(permissionGroup))){
                val itemBinding = PermissionxsPermissionItemBinding.inflate(layoutInflater, binding.permissionsLayout, false)
                when {
                    permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                        itemBinding.permissionText.text = "Background location"
                        itemBinding.permissionIcon.setImageResource(context.packageManager.getPermissionGroupInfo(permissionGroup!!, 0).icon)
                    }
                    permission == Manifest.permission.SYSTEM_ALERT_WINDOW -> {
                        itemBinding.permissionText.text = "Display over other apps"
                        itemBinding.permissionIcon.setImageResource(R.drawable.permissionxs_ic_alert)
                    }
                    permission == Manifest.permission.WRITE_SETTINGS -> {
                        itemBinding.permissionText.text = "Modify system settings"
                        itemBinding.permissionIcon.setImageResource(R.drawable.permissionxs_ic_setting)
                    }
                    permission == Manifest.permission.MANAGE_EXTERNAL_STORAGE -> {
                        itemBinding.permissionText.text = "All files access"
                        itemBinding.permissionIcon.setImageResource(R.drawable.permissionxs_ic_install)
                    }
                }
            }
        }

    }

    override fun getPositiveButton(): View {
        TODO("Not yet implemented")
    }

    override fun getNegativeButton(): View {
        TODO("Not yet implemented")
    }

    override fun getPermissionToRequest(): MutableList<String> {
        TODO("Not yet implemented")
    }


}