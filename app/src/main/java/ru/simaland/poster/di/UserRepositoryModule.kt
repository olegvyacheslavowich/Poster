package ru.simaland.poster.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.simaland.poster.repository.user.UserRepository
import ru.simaland.poster.repository.user.UserRepositoryImpl
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
interface UserRepositoryModule {

    @Singleton
    @Binds
    fun provideUserRepository(userRepository: UserRepositoryImpl): UserRepository

}