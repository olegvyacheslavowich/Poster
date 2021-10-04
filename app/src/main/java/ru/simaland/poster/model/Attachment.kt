package ru.simaland.poster.model

import ru.simaland.poster.entity.AttachmentEmbeddable

data class Attachment(
    val url: String,
    val type: AttachmentType,
)

fun Attachment.toEntity() = AttachmentEmbeddable(url, type)

enum class AttachmentType {
    IMAGE
}

