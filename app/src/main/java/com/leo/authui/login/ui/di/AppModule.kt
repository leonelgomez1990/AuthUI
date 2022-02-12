package com.leo.authui.login.ui.di

import com.leo.authui.login.ui.viewmodels.SignInViewModel
import com.leo.authui.login.ui.viewmodels.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideSplashViewModel () : SplashViewModel = SplashViewModel()

    @Provides
    fun provideSignInViewModel () : SignInViewModel = SignInViewModel()

}
