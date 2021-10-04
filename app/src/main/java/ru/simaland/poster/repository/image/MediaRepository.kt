package ru.simaland.poster.repository.image

import ru.simaland.poster.Media
import ru.simaland.poster.MediaUpload

interface MediaRepository {

    suspend fun upload(mediaUpload: MediaUpload): Media
}