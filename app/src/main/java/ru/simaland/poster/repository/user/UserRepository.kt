package ru.simaland.poster.repository.user

import ru.simaland.poster.model.User

interface UserRepository {

    suspend fun getUsersById(id: List<Int>): List<User>
}