package com.leo.authui.core.data.storage

import com.leo.authui.core.utils.MyResult

interface StorageRepository {

    suspend fun uploadFile(localPath: String, remotePath: String, name: String? = null): MyResult<String>
    suspend fun deleteFile(path: String): MyResult<Boolean>

    suspend fun uploadImage(localPath: String, remotePath: String, name: String? = null): MyResult<String>
    suspend fun deleteImage(path: String): MyResult<Boolean>
}