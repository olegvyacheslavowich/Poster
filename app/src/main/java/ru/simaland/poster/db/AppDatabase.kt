package ru.simaland.poster.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.simaland.poster.db.entity.UserEntity
import ru.simaland.poster.entity.EventEntity

@Database(
    entities = [UserEntity::class, EventEntity::class],
    version = AppDatabase.dbVersion
)
@TypeConverters(DataBaseTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val dbVersion = 3
        const val dbName = "posterDb"
    }

    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
}