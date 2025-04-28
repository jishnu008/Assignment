package com.android.assignment.local.Dao

import androidx.room.*
import com.android.assignment.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM offline_users WHERE isPendingSync = 1")
    suspend fun getPendingSyncUsers(): List<User>

    @Query("DELETE FROM offline_users WHERE localId = :localId")
    suspend fun delete(localId: Long)

    @Query("SELECT * FROM offline_users")
    fun getAllOfflineUsers(): Flow<List<User>>
}