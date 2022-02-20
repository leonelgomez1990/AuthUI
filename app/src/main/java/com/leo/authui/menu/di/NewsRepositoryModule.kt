package com.leo.authui.menu.di

import com.leo.authui.menu.data.NewsRepository
import com.leo.authui.menu.data.NewsRepositoryImp
import com.leo.authui.menu.framework.NewsProvider
import com.leo.authui.menu.usecases.GetNewUseCase
import com.leo.authui.menu.usecases.GetNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsRepositoryModule {

    @Provides
    @Singleton
    fun providerNewsRepository(provider: NewsProvider): NewsRepository = NewsRepositoryImp(provider)

    @Provides
    fun provideGetNewUseCase(newsRepository: NewsRepository): GetNewUseCase = GetNewUseCase(newsRepository)

    @Provides
    fun provideGetNewsUseCase(newsRepository: NewsRepository): GetNewsUseCase = GetNewsUseCase(newsRepository)
}