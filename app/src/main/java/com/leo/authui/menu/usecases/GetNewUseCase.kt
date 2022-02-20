package com.leo.authui.menu.usecases

import com.leo.authui.core.utils.MyResult
import com.leo.authui.menu.data.NewsRepository
import com.leo.authui.menu.domain.News

class GetNewUseCase (val newsRepository: NewsRepository) {
    suspend operator fun invoke(news : String) : MyResult<News> = newsRepository.getNew(news)
}