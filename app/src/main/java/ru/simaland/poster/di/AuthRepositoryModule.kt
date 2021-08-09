package ru.simaland.poster.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.simaland.poster.repository.auth.AuthRepository
import ru.simaland.poster.repository.auth.AuthRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthRepositoryModule {

    @Binds
    @Singleton
    fun provideAuthRepository(authRepository: AuthRepositoryImpl): AuthRepository

}