package com.leo.authui.core.framework.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.leo.authui.core.data.permission.PermissionSource
import com.leo.authui.core.utils.MyResult

class PermissionManager(
    private val permissionSource: PermissionSource,
    private val context: Context
) {

    fun requestStoragePermission(activity: Activity): MyResult<Boolean> {
        return if (!checkPermissions(permissionSource.getStoragePermission())) {
            requestPermissions(permissionSource.getStoragePermission(), activity)

            MyResult.Success(true)
        } else {
            MyResult.Failure(Exception())
        }
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
        var permissionState = 0

        for (perm in permissions) {
            permissionState += ActivityCompat.checkSelfPermission(context, perm)
        }
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(permissions: Array<String>, activity: Activity) {

        for (perm in permissions) {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)
        }
        startPermissionRequest(permissions, activity)
    }

    private fun startPermissionRequest(permissions: Array<String>, activity: Activity) {
        ActivityCompat.requestPermissions(activity, permissions, 0)
    }

}