package com.leo.authui.core.usecases

import android.app.Activity
import com.leo.authui.core.framework.permission.PermissionManager

class RequestPermissionsUseCase(private val permissionManager: PermissionManager) {

    fun requestStoragePermission(activity: Activity) =
        permissionManager.requestStoragePermission(activity)

}