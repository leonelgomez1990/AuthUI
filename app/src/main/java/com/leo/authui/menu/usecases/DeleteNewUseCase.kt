package com.leo.authui.menu.usecases

import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.data.NewsRepository

class DeleteNewUseCase (private val newsRepository: NewsRepository){
    suspend operator fun invoke(uid: String): MyResult<Boolean> =
        newsRepository.deleteNew(uid)
}
