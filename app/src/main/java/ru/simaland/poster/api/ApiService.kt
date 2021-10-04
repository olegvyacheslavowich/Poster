package ru.simaland.poster.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.simaland.poster.Media
import ru.simaland.poster.model.Event

interface ApiService {

    @Multipart
    @POST("api/media")
    suspend fun uploadMedia(@Part media: MultipartBody.Part): Response<Media?>

    @GET("api/events")
    suspend fun readAllEvents(): Response<List<Event>>

    @GET("api/events/latest")
    suspend fun readLastEvents(@Query("count") count: Int): Response<List<Event>>

    @POST("api/events")
    suspend fun saveEvent(@Body event: Event): Response<Event>


}