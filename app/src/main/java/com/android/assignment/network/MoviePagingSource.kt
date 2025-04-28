package com.android.assignment.network

import androidx.paging.PagingSource
import androidx.paging.PagingState

import com.android.assignment.local.Dao.MovieDao
import com.android.assignment.local.entity.CachedMovie
import com.android.assignment.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val movieApiService: MovieApiService,
    private val apiKey: String,
    private val movieDao: MovieDao
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = movieApiService.getTrendingMovies(apiKey = apiKey, page = page)
            val movies = response.movieList ?: emptyList()

            // Cache the fetched movies
            withContext(Dispatchers.IO) {
                movieDao.insertMovieList(movies.map {
                    CachedMovie(
                        id = it.id ?: 0,
                        title = it.title.orEmpty(),
                        posterPath = it.posterPath.orEmpty(),
                        backdropPath = it.backdropPath,
                        timestamp = System.currentTimeMillis(),

                    )
                })
                movieDao.clearOldCache(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7))
            }

            LoadResult.Page(
                data = movies,
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (response.totalPages > page) page + 1 else null
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}