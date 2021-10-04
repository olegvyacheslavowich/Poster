package ru.simaland.poster.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.simaland.poster.repository.event.EventRepository
import ru.simaland.poster.repository.event.EventRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface EventRepositoryModule {

    @Binds
    @Singleton
    fun provideEventRepository(eventRepositoryImpl: EventRepositoryImpl): EventRepository

}