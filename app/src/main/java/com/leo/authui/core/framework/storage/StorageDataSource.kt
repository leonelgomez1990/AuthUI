package com.leo.authui.core.framework.storage

import com.leo.authui.core.utils.MyResult

interface StorageDataSource {

    suspend fun uploadFile(localPath: String, remotePath: String, name: String? = null): MyResult<String>
    suspend fun deleteFile(path: String): MyResult<Boolean>

    suspend fun uploadImage(localPath: String, remotePath: String, name: String? = null): MyResult<String>
    suspend fun deleteImage(path: String): MyResult<Boolean>
}