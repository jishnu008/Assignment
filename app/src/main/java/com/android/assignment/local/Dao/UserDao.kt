package com.android.assignment.local.Dao

import androidx.room.*
import com.android.assignment.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_entity")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM user_entity WHERE isPendingSync = 1")
    suspend fun getUnsyncedUsers(): List<UserEntity>

    @Query("DELETE FROM user_entity")
    suspend fun deleteAllUsers()

    @androidx.room.Query("SELECT * FROM user_entity WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @androidx.room.Query("SELECT * FROM user_entity LIMIT :limit OFFSET :offset")
    suspend fun getAllUsersDirectly(offset: Int, limit: Int): List<UserEntity>

    suspend fun getUsersPaged(offset: Int, limit: Int): List<UserEntity> =
        this.getAllUsersDirectly(offset, limit)
}