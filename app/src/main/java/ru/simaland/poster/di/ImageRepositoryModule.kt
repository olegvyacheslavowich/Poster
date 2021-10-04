package ru.simaland.poster.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.simaland.poster.repository.image.MediaRepository
import ru.simaland.poster.repository.image.MediaRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface ImageRepositoryModule {

    @Binds
    @Singleton
    fun bindMediaRepository(mediaRepository: MediaRepositoryImpl): MediaRepository

}