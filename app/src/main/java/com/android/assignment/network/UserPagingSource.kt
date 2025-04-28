package com.android.assignment.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.assignment.local.Dao.UserDao
import com.android.assignment.model.UserEntity
import com.android.assignment.model.UserResponse
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class UserPagingSource(
    private val userApi: UserApi,
    private val userDao: UserDao
) : PagingSource<Int, UserEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> {

        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = userApi.getUsers(page)
            val users = response.data.map { it.toUserEntity() }
            userDao.insertAll(users) // Cache in local database
            LoadResult.Page(
                data = users,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.data.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

// Extension function to map UserResponse.Data to UserEntity
fun UserResponse.Data.toUserEntity(): UserEntity {
    return UserEntity(
        remoteId = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        avatar = this.avatar,
        jobTitle = null,
        isPendingSync = false
    )
}