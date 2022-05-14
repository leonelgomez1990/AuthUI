package com.leo.authui.core.data.permission

import android.Manifest

class PermissionSourceImpl : PermissionSource {

    override fun getStoragePermission(): Array<String> {
        return arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun getLocationPermission(): Array<String> {
        return arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun getCameraPermission(): Array<String> {
        return arrayOf(
            Manifest.permission.CAMERA
        )
    }
}