package com.android.assignment.local.Dao

import androidx.room.*
import com.android.assignment.local.entity.CachedMovieDetail

import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: CachedMovieDetail)


    @Query("SELECT * FROM movie_details WHERE id = :movieId")
    suspend fun getCachedMovieDetail(movieId: Int): CachedMovieDetail?

    @Query("SELECT * FROM movie_details WHERE id = :movieId")
    fun getCachedMovieDetailFlow(movieId: Int): Flow<CachedMovieDetail?>

    @Query("DELETE FROM movie_details WHERE timestamp < :cutoff")
    suspend fun clearOldCache(cutoff: Long)


}