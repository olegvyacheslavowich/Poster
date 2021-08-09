package ru.simaland.poster.repository.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.simaland.poster.MediaUpload
import ru.simaland.poster.api.AuthApiService
import ru.simaland.poster.auth.Auth
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRepository {

    override suspend fun register(
        mediaUpload: MediaUpload,
        name: String,
        login: String,
        password: String
    ): Flow<Auth> = flow {
        val response = api.register(
            MultipartBody.Part.createFormData("name", name),
            MultipartBody.Part.createFormData("login", login),
            MultipartBody.Part.createFormData("pass", password),
            MultipartBody.Part.createFormData(
                "file", mediaUpload.file.name, mediaUpload.file.asRequestBody()
            )
        )

        if (!response.isSuccessful) {
            throw Exception(response.message())
        }
        val auth = response.body() ?: Auth()
        emit(auth)
    }.catch { e ->
        throw e
    }.flowOn(Dispatchers.Default)


    override suspend fun register(name: String, login: String, password: String): Flow<Auth> =
        flow {
            val response = api.register(name, login, password)
            if (!response.isSuccessful) {
                throw Exception(response.message())
            }
            val auth = response.body() ?: Auth()
            emit(auth)
        }.catch { e ->
            throw e
        }.flowOn(Dispatchers.Default)


    override suspend fun login(login: String, password: String): Flow<Auth> = flow {
        val response = api.login(login, password)
        if (!response.isSuccessful) {
            throw Exception(response.message())
        }
        val auth = response.body() ?: Auth()
        emit(auth)
    }.catch { e ->
        throw e
    }.flowOn(Dispatchers.Default)
}