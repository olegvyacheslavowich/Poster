package ru.simaland.poster.model

data class Post(
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String = "",
    val authorAvatar: String = "",
    val content: String = "",
    val published: Long = 0,
    val coords: Coordinate = Coordinate(),
    val attachment: Attachment? = null

)
