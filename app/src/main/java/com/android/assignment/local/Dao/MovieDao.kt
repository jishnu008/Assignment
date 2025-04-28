package com.android.assignment.local.Dao

import androidx.room.*
import com.android.assignment.local.entity.CachedMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movies: List<CachedMovie>)

    @Query("SELECT * FROM movie_list ORDER BY timestamp DESC")
    fun getCachedMovieList(): Flow<List<CachedMovie>>

    @Query("DELETE FROM movie_list WHERE timestamp < :cutoff")
    suspend fun clearOldCache(cutoff: Long)

    @Query("SELECT * FROM movie_list WHERE id = :movieId")
    suspend fun getCachedMovieById(movieId: Int): CachedMovie?

    @Query("DELETE FROM movie_list")
    suspend fun clearAllMoviesCache()
}