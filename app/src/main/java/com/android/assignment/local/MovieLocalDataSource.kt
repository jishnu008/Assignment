package com.android.assignment.local

import com.android.assignment.local.entity.CachedMovie
import com.android.assignment.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    suspend fun insertTrendingMovies(movies: List<CachedMovie>)
    fun getCachedTrendingMovies(): Flow<List<CachedMovie>>
    suspend fun saveMovies(movies: List<Movie>)
    suspend fun getAllMovies(): List<Movie>
    suspend fun getMovieById(movieId: Int): Movie?
    suspend fun clearAllMovies()
}