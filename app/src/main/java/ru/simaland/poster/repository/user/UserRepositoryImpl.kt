package ru.simaland.poster.repository.user

import ru.simaland.poster.api.AuthApiService
import ru.simaland.poster.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) :
    UserRepository {

    override suspend fun getUsersById(ids: List<Int>): List<User> {
        val response = authApiService.getUsers()
        if (!response.isSuccessful) {
            throw Exception(response.message())
        }

        val users = response.body() ?: emptyList()
        if (users.isEmpty()) {
            throw Exception("Something went wrong")
        }

        return users.filter { user -> ids.contains(user.id) }
    }
}