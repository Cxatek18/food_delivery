package com.team.fooddelivery.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.team.fooddelivery.data.repository.UserRepositoryImpl
import com.team.fooddelivery.domain.repository.user.UserRepository
import com.team.fooddelivery.domain.usecase.user.AuthEmailAndPasswordUseCase
import com.team.fooddelivery.domain.usecase.user.GetCurrentUserUseCase
import com.team.fooddelivery.domain.usecase.user.SendVerifyCodeUseCase
import com.team.fooddelivery.domain.usecase.user.SignInWithEmailAndPasswordUseCase
import com.team.fooddelivery.domain.usecase.user.VerifyCodeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetCurrentUserUseCase(
        repository: UserRepository
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(repository)
    }

    @Provides
    fun provideAuthEmailAndPasswordUseCase(
        repository: UserRepository
    ): AuthEmailAndPasswordUseCase {
        return AuthEmailAndPasswordUseCase(repository)
    }

    @Provides
    fun provideSendVerifyCodeUseCase(
        repository: UserRepository
    ): SendVerifyCodeUseCase {
        return SendVerifyCodeUseCase(repository)
    }

    @Provides
    fun provideVerifyCodeUseCase(
        repository: UserRepository
    ): VerifyCodeUseCase {
        return VerifyCodeUseCase(repository)
    }

    @Provides
    fun provideSignInWithEmailAndPasswordUseCase(
        repository: UserRepository
    ): SignInWithEmailAndPasswordUseCase {
        return SignInWithEmailAndPasswordUseCase(repository)
    }

    @Provides
    fun provideUserRepository(
        auth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase
    ): UserRepository {
        return UserRepositoryImpl(auth, firebaseDatabase)
    }
}