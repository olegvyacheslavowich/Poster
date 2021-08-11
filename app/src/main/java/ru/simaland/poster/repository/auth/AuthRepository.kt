package ru.simaland.poster.repository.auth

import kotlinx.coroutines.flow.Flow
import ru.simaland.poster.MediaUpload
import ru.simaland.poster.auth.Auth
import ru.simaland.poster.model.User

interface AuthRepository {

    suspend fun register(
        mediaUpload: MediaUpload,
        name: String,
        login: String,
        password: String
    ): Flow<Auth>

    suspend fun register(
        name: String,
        login: String,
        password: String
    ): Flow<Auth>

    suspend fun login(login: String, password: String): Flow<Auth>

    suspend fun saveCurrentUser(userId: Int)

    suspend fun getCurrentUser(): User
}