package ru.simaland.poster.model

import ru.simaland.poster.db.entity.UserEntity

data class User(
    val id: Int = 0,
    val login: String = "",
    val name: String = "",
    val avatar: String? = ""
)


fun User.toEntity() =
    UserEntity(id, login, name, avatar)