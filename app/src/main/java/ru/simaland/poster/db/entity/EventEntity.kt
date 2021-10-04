package ru.simaland.poster.entity

import androidx.room.*
import ru.simaland.poster.model.Attachment
import ru.simaland.poster.model.AttachmentType
import ru.simaland.poster.model.Event

@Entity(tableName = "Event")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String = "",
    val authorAvatar: String = "",
    val content: String = "",
    val published: String = "",
    val datetime: String = "",
    val type: String = "",
    val link: String = "",
    val speakersId: List<Int> = emptyList(),
    @Embedded(prefix = "attachment_")
    val attachment: AttachmentEmbeddable? = null
)

fun EventEntity.toDto() =
    Event(
        id,
        authorId,
        author,
        authorAvatar,
        content,
        published,
        datetime,
        type,
        link,
        speakersId,
        attachment?.toDto()
    )

fun List<EventEntity>.toDto() = this.map { it.toDto() }


data class AttachmentEmbeddable(
    val url: String,
    val type: AttachmentType,
)

fun AttachmentEmbeddable.toDto() = Attachment(url, type)

