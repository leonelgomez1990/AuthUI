package com.leo.authui.core.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.leo.authui.core.data.permission.PermissionSource
import com.leo.authui.core.data.permission.PermissionSourceImpl
import com.leo.authui.core.data.storage.StorageRepository
import com.leo.authui.core.data.storage.StorageRepositoryImpl
import com.leo.authui.core.framework.permission.PermissionManager
import com.leo.authui.core.framework.storage.FirebaseStorageDataSource
import com.leo.authui.core.framework.storage.StorageDataSource
import com.leo.authui.core.usecases.RequestPermissionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    // Framework provides
    @Singleton
    @Provides
    fun providesFirestore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun providesStorage(): FirebaseStorage = Firebase.storage

    // Framework- DataSource provides
    @Singleton
    @Provides
    fun provideFirebaseStorageDataSource(
        storage: FirebaseStorage,
        @ApplicationContext context: Context
    ): StorageDataSource =
        FirebaseStorageDataSource(storage, context)

    @Provides
    fun providePermissionManager(
        @ApplicationContext context: Context,
        source: PermissionSource
    ): PermissionManager =
        PermissionManager(source, context)

    // Data- Repository provides
    @Provides
    @Singleton
    fun provideStorageRepository(storageDataSource: StorageDataSource): StorageRepository =
        StorageRepositoryImpl(storageDataSource)

    @Provides
    fun providePermissionSource(): PermissionSource = PermissionSourceImpl()

    // Use cases
    @Provides
    fun provideRequestPermissionsUseCase(permissionManager: PermissionManager): RequestPermissionsUseCase =
        RequestPermissionsUseCase(permissionManager)

}