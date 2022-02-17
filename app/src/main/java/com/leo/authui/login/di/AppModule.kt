package com.leo.authui.login.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leo.authui.login.data.UsersDataSource
import com.leo.authui.login.data.UsersRepository
import com.leo.authui.login.framework.FirebaseUserSource
import com.leo.authui.login.ui.viewmodels.SignInViewModel
import com.leo.authui.login.ui.viewmodels.SplashViewModel
import com.leo.authui.login.usecases.LoginWithEmailAndPasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    // Framework provides
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    // Data- DataSource provides
    @Provides
    fun provideFirebaseUserSource(auth: FirebaseAuth) : UsersDataSource = FirebaseUserSource(auth)

    // Data- Repository provides
    @Provides
    fun provideUsersRepository(userDataSource : UsersDataSource) :UsersRepository = UsersRepository(userDataSource)

    // Usecases provides
    @Provides
    fun provideLoginWithEmailAndPasswordUseCase(usersRepository: UsersRepository): LoginWithEmailAndPasswordUseCase = LoginWithEmailAndPasswordUseCase(usersRepository)

    // Viewmodel provides
    @Provides
    fun provideSignInViewModel (loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase) : SignInViewModel = SignInViewModel(loginWithEmailAndPasswordUseCase)

    @Provides
    fun provideSplashViewModel () : SplashViewModel = SplashViewModel()


}