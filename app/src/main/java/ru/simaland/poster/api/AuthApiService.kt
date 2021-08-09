package ru.simaland.poster.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import ru.simaland.poster.auth.Auth

interface AuthApiService {

    @Multipart
    @POST("/api/users/registration")
    suspend fun register(
        @Part name: MultipartBody.Part,
        @Part login: MultipartBody.Part,
        @Part pass: MultipartBody.Part,
        @Part avatar: MultipartBody.Part
    ): Response<Auth>

    @FormUrlEncoded
    @POST("/api/users/registration")
    suspend fun register(
        @Field("name") name: String,
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<Auth>

    @FormUrlEncoded
    @POST("/api/users/authentication")
    suspend fun login(
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<Auth>
}