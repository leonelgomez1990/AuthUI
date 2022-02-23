package com.leo.authui.menu.usecases

import com.leo.authui.core.utils.MyResult
import com.leo.authui.core.data.storage.StorageRepository

class DeleteImageUseCase(private val storageRepository: StorageRepository) {
    suspend operator fun invoke(path: String): MyResult<Boolean> =
        storageRepository.deleteImage(path)
}

