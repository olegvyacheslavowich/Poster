package ru.simaland.poster.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.simaland.poster.db.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = AppDatabase.dbVersion
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val dbVersion = 1
        const val dbName = "posterDb"
    }

    abstract fun posterDao(): PosterDao
}