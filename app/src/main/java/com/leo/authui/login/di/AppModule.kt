package com.leo.authui.login.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leo.authui.login.data.UsersRepository
import com.leo.authui.login.data.UsersRepositoryImpl
import com.leo.authui.login.framework.FirebaseUserSource
import com.leo.authui.login.framework.UsersDataSource
import com.leo.authui.login.ui.viewmodels.SignInViewModel
import com.leo.authui.login.ui.viewmodels.SignUpViewModel
import com.leo.authui.login.ui.viewmodels.SplashViewModel
import com.leo.authui.login.usecases.CreateUserWithEmailAndPasswordUseCase
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
    fun provideUsersRepository(usersDataSource: UsersDataSource) : UsersRepository = UsersRepositoryImpl(usersDataSource)

    // Usecases provides
    @Provides
    fun provideLoginWithEmailAndPasswordUseCase(userRepository: UsersRepository) : LoginWithEmailAndPasswordUseCase = LoginWithEmailAndPasswordUseCase(userRepository)

    @Provides
    fun provideCreateUserWithEmailAndPasswordUseCase(userRepository: UsersRepository) : CreateUserWithEmailAndPasswordUseCase = CreateUserWithEmailAndPasswordUseCase(userRepository)

    // Viewmodel provides
    @Provides
    fun provideSignInViewModel (loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase) : SignInViewModel = SignInViewModel(loginWithEmailAndPasswordUseCase)

    @Provides
    fun provideSignUpViewModel (createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase) : SignUpViewModel = SignUpViewModel(createUserWithEmailAndPasswordUseCase)

    @Provides
    fun provideSplashViewModel () : SplashViewModel = SplashViewModel()


}
