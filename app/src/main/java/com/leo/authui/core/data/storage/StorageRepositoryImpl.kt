package com.leo.authui.core.data.storage

import com.leo.authui.core.framework.storage.StorageDataSource
import com.leo.authui.core.utils.MyResult

class StorageRepositoryImpl(private val storageDataSource: StorageDataSource) : StorageRepository {

    override suspend fun uploadFile(
        localPath: String,
        remotePath: String,
        name: String?
    ): MyResult<String> {
        return storageDataSource.uploadFile(localPath, remotePath, name)
    }

    override suspend fun deleteFile(path: String): MyResult<Boolean> {
        return storageDataSource.deleteFile(path)
    }

    override suspend fun uploadImage(
        localPath: String,
        remotePath: String,
        name: String?
    ): MyResult<String> {
        return storageDataSource.uploadImage(localPath, remotePath, name)
    }

    override suspend fun deleteImage(path: String): MyResult<Boolean> {
        return storageDataSource.deleteImage(path)
    }

}