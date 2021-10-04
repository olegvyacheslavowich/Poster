package ru.simaland.poster.repository.event

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.simaland.poster.MediaUpload
import ru.simaland.poster.api.ApiService
import ru.simaland.poster.db.EventDao
import ru.simaland.poster.entity.toDto
import ru.simaland.poster.model.Attachment
import ru.simaland.poster.model.AttachmentType
import ru.simaland.poster.model.Event
import ru.simaland.poster.model.toEntity
import ru.simaland.poster.repository.image.MediaRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val dao: EventDao,
    private val mediaRepository: MediaRepository
) : EventRepository {

    override val data: Flow<List<Event>> =
        dao.getData().map { it.toDto() }
            .flowOn(Dispatchers.IO)

    override suspend fun readAll() {
        try {
            val response = service.readAllEvents()
            if (!response.isSuccessful) {
                throw Exception(response.message())
            }
            val events = response.body()?.map { it.toEntity() } ?: emptyList()
            dao.createData(events)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun readLast(count: Int) {
        try {
            val response = service.readLastEvents(count)
            if (!response.isSuccessful) {
                throw Exception(response.message())
            }
            val events = response.body()?.map { it.toEntity() } ?: emptyList()
            dao.createData(events)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun save(event: Event, mediaUpload: MediaUpload?): Event {
        try {
            val response = if (mediaUpload != null) {
                val media = mediaRepository.upload(mediaUpload)
                service.saveEvent(
                    event.copy(
                        attachment = Attachment(
                            media.url,
                            AttachmentType.IMAGE
                        )
                    )
                )
            } else {
                service.saveEvent(event)
            }
            if (!response.isSuccessful) throw Exception(response.message())
            with(response.body() ?: throw Exception(response.message())) {
                dao.create(this.toEntity())
                return this
            }

        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun save(event: Event): Event =
        try {
            val response = service.saveEvent(event)
            if (!response.isSuccessful) throw Exception(response.message())
            dao.create(event.toEntity())
            response.body() ?: throw Exception(response.message())
        } catch (e: Exception) {
            throw e
        }
}



