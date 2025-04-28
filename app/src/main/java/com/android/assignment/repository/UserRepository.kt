package com.android.assignment.repository

import androidx.paging.PagingSource
import com.android.assignment.local.Dao.OfflineUserDao
import com.android.assignment.local.Dao.UserDao
import com.android.assignment.model.User
import com.android.assignment.model.UserEntity
import com.android.assignment.model.UserRequest
import com.android.assignment.model.UserResponse
import com.android.assignment.network.UserApi
import com.android.assignment.network.UserPagingSource
import com.android.assignment.sync.OfflineSyncManager
import com.android.assignment.ui.userlist.LocalUserPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val offlineUserDao: OfflineUserDao,
    private val offlineSyncManager: OfflineSyncManager
) {

    fun getUsersStream(isOnline: Boolean): PagingSource<Int, UserEntity> {
        return if (isOnline) {
            UserPagingSource(userApi, userDao)
        } else {
            LocalUserPagingSource(userDao)
        }
    }
    suspend fun addUser(firstName: String, lastName: String, jobTitle: String, isOnline: Boolean): Flow<Result<UserEntity?>> = flow {
        val userRequest = UserRequest(name = firstName, job = jobTitle)
        if (isOnline) {
            try {
                val response = userApi.addUser(userRequest)
                val newUser = response.data.firstOrNull()
                newUser?.let {
                    val userEntity = it.toUserEntity(firstName, lastName, jobTitle)
                    userDao.insertUser(userEntity)
                    emit(Result.success(userEntity))
                } ?: emit(Result.success(null))
            } catch (e: Exception) {
                val userEntity = UserEntity(firstName = firstName, lastName = lastName, avatar = "", jobTitle = jobTitle, isPendingSync = true)
                val localId = offlineUserDao.insert(userEntity.toOfflineUser())
                emit(Result.success(userEntity.copy(id = localId.toInt())))
                offlineSyncManager.scheduleSync()
            }
        } else {
            val userEntity = UserEntity(firstName = firstName, lastName = lastName, avatar = "", jobTitle = jobTitle, isPendingSync = true)
            val localId = offlineUserDao.insert(userEntity.toOfflineUser())
            emit(Result.success(userEntity.copy(id = localId.toInt())))
            offlineSyncManager.scheduleSync()
        }
    }.flowOn(Dispatchers.IO)

    suspend fun syncOfflineUsers(): Flow<Result<Unit>> = flow {
        val pendingUsers = offlineUserDao.getPendingSyncUsers()
        for (offlineUser in pendingUsers) {
            val userRequest = UserRequest(name = offlineUser.firstName, job = offlineUser.jobTitle)
            try {
                val response = userApi.addUser(userRequest)
                val syncedUser = response.data.firstOrNull()
                syncedUser?.let {
                    userDao.insertUser(offlineUser.toUserEntity(it.id))
                    offlineUserDao.delete(offlineUser.localId)
                    emit(Result.success(Unit))
                } ?: emit(Result.failure(Exception("Failed to get synced user data")))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
        emit(Result.success(Unit))
    }.flowOn(Dispatchers.IO)

    fun addUserOffline(user: User): Flow<Result<Long>> = flow {
        try {
            val localId = offlineUserDao.insert(user)
            emit(Result.success(localId))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getAllLocalUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun updateUserId(localId: Int, remoteId: Int) {
        userDao.getUserById(localId)?.let {
            userDao.updateUser(it.copy(remoteId = remoteId))
        }
    }

    suspend fun getUnsyncedUsers(): List<UserEntity> = userDao.getUnsyncedUsers()

    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }
}

fun UserResponse.Data.toUserEntity(firstName: String, lastName: String, jobTitle: String): UserEntity {
    return UserEntity(
        remoteId = this.id,
        firstName = firstName,
        lastName = lastName,
        avatar = this.avatar,
        jobTitle = jobTitle,
        isPendingSync = false
    )
}

fun UserEntity.toOfflineUser(): User {
    return User(
        localId = this.id.toLong(),
        firstName = this.firstName,
        lastName = this.lastName,
        email = "",
        avatar = this.avatar,
        isPendingSync = this.isPendingSync,
        remoteId = this.remoteId,
        jobTitle = this.jobTitle ?: ""
    )
}

fun User.toUserEntity(remoteId: Int? = null): UserEntity {
    return UserEntity(
        id = this.localId.toInt(),
        firstName = this.firstName,
        lastName = this.lastName,
        avatar = this.avatar ?: "",
        jobTitle = this.jobTitle,
        remoteId = remoteId,
        isPendingSync = false
    )
}