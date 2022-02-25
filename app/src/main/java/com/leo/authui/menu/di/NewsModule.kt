package com.leo.authui.menu.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.leo.authui.core.data.storage.StorageRepository
import com.leo.authui.menu.data.NewsRepository
import com.leo.authui.menu.data.NewsRepositoryImpl
import com.leo.authui.menu.framework.FirebaseNewsDataSource
import com.leo.authui.menu.framework.NewsDataSource
import com.leo.authui.menu.framework.NewsProvider
import com.leo.authui.menu.framework.NewsProviderImpl
import com.leo.authui.menu.ui.viewmodels.EditNewViewModel
import com.leo.authui.menu.usecases.*
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
    //@Provides
    //fun provideRetrofitNewsDataSource(newsProvider: NewsProvider): NewsDataSource =
    //    NewsProviderImpl(newsProvider)

    @Singleton
    @Provides
    fun provideFirebaseNewsDataSource(
        db: FirebaseFirestore,
        storage: FirebaseStorage
    ): NewsDataSource =
        FirebaseNewsDataSource(db, storage)

    // Data- Repository provides
    @Provides
    @Singleton
    fun provideNewsRepository(newsDataSource: NewsDataSource): NewsRepository =
        NewsRepositoryImpl(newsDataSource)

    // Usecases provides
    @Provides
    fun provideGetNewsUseCase(newsRepository: NewsRepository): GetNewsUseCase =
        GetNewsUseCase(newsRepository)

    @Provides
    fun provideDeleteNewUseCase(newsRepository: NewsRepository): DeleteNewUseCase =
        DeleteNewUseCase(newsRepository)

    @Provides
    fun provideUpdateNewUseCase(newsRepository: NewsRepository): UpdateNewUseCase =
        UpdateNewUseCase(newsRepository)

    // Usecases provides
    @Provides
    fun provideUploadImageUseCase(storageRepository: StorageRepository): UploadImageUseCase =
        UploadImageUseCase(storageRepository)

    @Provides
    fun provideDeleteImageUseCase(storageRepository: StorageRepository): DeleteImageUseCase =
        DeleteImageUseCase(storageRepository)

    @Provides
    fun provideCreateNewUseCase(newsRepository: NewsRepository): CreateNewUseCase =
        CreateNewUseCase(newsRepository)


    // Viewmodel provides
    @Provides
    fun provideEditNewViewModel(
        updateNewUseCase: UpdateNewUseCase,
        getNewUseCase: GetNewsUseCase
    ): EditNewViewModel =
        EditNewViewModel(updateNewUseCase, getNewUseCase)


}