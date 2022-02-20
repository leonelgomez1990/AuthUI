package com.leo.authui.menu.di

import com.leo.authui.menu.data.NewsRepository
import com.leo.authui.menu.data.NewsRepositoryImpl
import com.leo.authui.menu.framework.NewsDataSource
import com.leo.authui.menu.framework.NewsProvider
import com.leo.authui.menu.framework.NewsProviderImpl
import com.leo.authui.menu.usecases.GetNewsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = "https://newsapi.org/v2/".toHttpUrl()

    @Singleton
    @Provides
    fun provideRetrofit(@Named("BaseUrl") baseUrl: HttpUrl): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun providerNewsProvider(retrofit: Retrofit): NewsProvider =
        retrofit.create(NewsProvider::class.java)

    // Framework- DataSource provides
    @Provides
    fun provideNewsDataSource(newsProvider: NewsProvider) : NewsDataSource =
        NewsProviderImpl(newsProvider)

    // Data- Repository provides
    @Provides
    @Singleton
    fun provideNewsRepository(newsDataSource: NewsDataSource): NewsRepository =
        NewsRepositoryImpl(newsDataSource)

    // Usecases provides
    @Provides
    fun provideGetNewsUseCase(newsRepository: NewsRepository): GetNewsUseCase =
        GetNewsUseCase(newsRepository)
}