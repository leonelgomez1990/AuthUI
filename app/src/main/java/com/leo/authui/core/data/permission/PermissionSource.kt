package com.leo.authui.core.data.permission

interface PermissionSource {
    fun getStoragePermission(): Array<String>
    fun getLocationPermission(): Array<String>
    fun getCameraPermission(): Array<String>
}