package com.leo.authui.login.ui.di

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

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    // Framework provides

    // Data- DataSource provides
    @Provides
    fun provideFirebaseUserSource() : UsersDataSource = FirebaseUserSource()

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
