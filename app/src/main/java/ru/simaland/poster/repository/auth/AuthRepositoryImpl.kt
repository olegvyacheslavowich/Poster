package ru.simaland.poster.repository.auth

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.simaland.poster.MediaUpload
import ru.simaland.poster.api.AuthApiService
import ru.simaland.poster.auth.Auth
import ru.simaland.poster.db.UserDao
import ru.simaland.poster.db.entity.toDto
import ru.simaland.poster.model.User
import ru.simaland.poster.model.toEntity
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val dbDao: UserDao
) : AuthRepository {

    override suspend fun register(
        mediaUpload: MediaUpload,
        name: String,
        login: String,
        password: String
    ): Auth =
        try {
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
            response.body() ?: Auth()
        } catch (e: Exception) {
            throw e
        }


    override suspend fun register(name: String, login: String, password: String): Auth =
        try {
            val response = api.register(name, login, password)
            if (!response.isSuccessful) {
                throw Exception(response.message())
            }
            response.body() ?: Auth()
        } catch (e: Exception) {
            throw e
        }


    override suspend fun login(login: String, password: String): Auth =
        try {
            val response = api.login(login, password)
            if (!response.isSuccessful) {
                throw Exception(response.message())
            }
            val auth = response.body() ?: Auth()
            auth.let {
                if (it.id != 0) {
                    saveCurrentUser(auth.id)
                }
                it
            }
        } catch (e: Exception) {
            throw e
        }

    override suspend fun saveCurrentUser(userId: Int) {
        val response = api.getUsers()
        if (!response.isSuccessful) {
            throw Exception(response.message())
        }

        val users = response.body() ?: emptyList()
        if (users.isEmpty()) {
            throw Exception("Something went wrong")
        }

        val currentUser = users.find { it.id == userId } ?: throw Exception("Something went wrong")
        dbDao.updateUser(currentUser.toEntity())
    }

    override suspend fun getCurrentUser(): User = dbDao.getUser().toDto()

}