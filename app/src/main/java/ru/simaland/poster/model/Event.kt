package ru.simaland.poster.model

import ru.simaland.poster.entity.EventEntity

data class Event(
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String = "",
    val authorAvatar: String = "",
    val content: String = "",
    val published: String = "",
    val datetime: String = "",
    val type: String = "",
    val link: String = "",
    val speakerIds: List<Int> = emptyList(),
    val attachment: Attachment? = null
)

fun Event.toEntity() =
    EventEntity(
        id,
        authorId,
        author,
        authorAvatar,
        content,
        published,
        datetime,
        type,
        link,
        speakerIds,
        attachment?.toEntity()
    )

fun List<Event>.toEntity() = this.map { it.toEntity() }