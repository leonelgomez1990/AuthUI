package com.leo.authui.menu.usecases

import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.data.storage.StorageRepository

class UploadImageUseCase(private val storageRepository: StorageRepository) {
    suspend operator fun invoke(localPath: String, remotePath: String, name: String? = null): MyResult<String> =
        storageRepository.uploadImage(localPath, remotePath)
}

