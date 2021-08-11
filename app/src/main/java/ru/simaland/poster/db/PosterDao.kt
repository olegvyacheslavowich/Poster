package ru.simaland.poster.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.simaland.poster.db.entity.UserEntity

@Dao
interface PosterDao {

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(userEntity: UserEntity)

    @Query("DELETE FROM User")
    suspend fun removeUser()

    @Transaction
    suspend fun updateUser(userEntity: UserEntity) {
        removeUser()
        saveUser(userEntity)
    }

}