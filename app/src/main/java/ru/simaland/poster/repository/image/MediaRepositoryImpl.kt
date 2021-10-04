package ru.simaland.poster.repository.image

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.simaland.poster.Media
import ru.simaland.poster.MediaUpload
import ru.simaland.poster.api.ApiService
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val api: ApiService
) : MediaRepository {

    override suspend fun upload(mediaUpload: MediaUpload): Media {
        val media = MultipartBody.Part.createFormData(
            "file", mediaUpload.file.name, mediaUpload.file.asRequestBody()
        )
        val response = api.uploadMedia(media)
        if (!response.isSuccessful) {
            throw Exception(response.message())
        }
        return response.body() ?: throw Exception(response.message())
    }
}