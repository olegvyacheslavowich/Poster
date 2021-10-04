package ru.simaland.poster.db

import androidx.room.TypeConverter

class DataBaseTypeConverter {

    @TypeConverter
    fun fromSpeakers(participants: List<Int>): String {
        return participants.joinToString()
    }

    @TypeConverter
    fun toSpeakers(data: String): List<Int> {
        return data
            .trim()
            .split(",")
            .filter { it.isNotEmpty() }
            .map { it.trim().toInt() }
            .toList()
    }
}