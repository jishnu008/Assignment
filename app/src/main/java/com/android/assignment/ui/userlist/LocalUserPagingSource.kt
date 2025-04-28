package com.android.assignment.ui.userlist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.assignment.local.Dao.UserDao
import com.android.assignment.model.UserEntity

class LocalUserPagingSource(private val userDao: UserDao) : PagingSource<Int, UserEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserEntity> {
        val page = params.key ?: 0
        val pageSize = params.loadSize
        val users = userDao.getUsersPaged(page * pageSize, pageSize)
        val nextKey = if (users.size == pageSize) page + 1 else null
        val prevKey = if (page > 0) page - 1 else null
        return try {
            LoadResult.Page(
                data = users,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}