package ru.simaland.poster.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.simaland.poster.entity.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM Event ORDER BY id DESC")
    fun getData(): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createData(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(event: EventEntity)

}