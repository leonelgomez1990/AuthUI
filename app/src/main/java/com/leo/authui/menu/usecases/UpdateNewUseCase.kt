package com.leo.authui.menu.usecases

import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.data.NewsRepository
import com.leo.authui.menu.domain.News

class UpdateNewUseCase(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(data: News): MyResult<Boolean> =
        newsRepository.updateNew(data)
}

