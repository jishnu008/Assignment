package com.android.assignment.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.assignment.BuildConfig
import com.android.assignment.model.Movie
import com.android.assignment.model.MovieDetailResponse
import com.android.assignment.network.MovieApiService
import com.android.assignment.network.MoviePagingSource
import com.android.assignment.utlis.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton





interface MovieRepository {
    fun getTrendingMovies(): Flow<PagingData<Movie>>
    fun getCachedTrendingMovies(): Flow<List<Movie>>
    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetailResponse>
    fun getCachedMovieDetail(movieId: Int): Flow<Resource<MovieDetailResponse>>
}