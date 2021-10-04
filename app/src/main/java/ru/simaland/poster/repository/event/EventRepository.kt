package ru.simaland.poster.repository.event

import kotlinx.coroutines.flow.Flow
import ru.simaland.poster.MediaUpload
import ru.simaland.poster.model.Event

interface EventRepository {
    val data: Flow<List<Event>>
    suspend fun readAll()
    suspend fun readLast(count: Int)
    suspend fun save(event: Event, mediaUpload: MediaUpload?): Event
    suspend fun save(event: Event): Event
}