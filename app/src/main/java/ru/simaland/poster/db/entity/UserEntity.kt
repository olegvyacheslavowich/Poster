package ru.simaland.poster.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.simaland.poster.model.User

@Entity(tableName = "User")
data class UserEntity(
    @PrimaryKey
    val id: Int = 0,
    val login: String = "",
    val name: String = "",
    val avatar: String = ""
)

fun UserEntity.toDto() = User(id, login, name, avatar)