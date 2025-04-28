package com.android.assignment.local

import com.android.assignment.local.Dao.MovieDao
import com.android.assignment.local.entity.CachedMovie
import com.android.assignment.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(private val movieDao: MovieDao) : MovieLocalDataSource {
    override suspend fun insertTrendingMovies(movies: List<CachedMovie>) {
        movieDao.insertMovieList(movies)
    }

    override fun getCachedTrendingMovies(): Flow<List<CachedMovie>> {
        return movieDao.getCachedMovieList()
    }

    override suspend fun saveMovies(movies: List<Movie>) {
        val cachedMovies = movies.map { it.toCachedMovie() }
        movieDao.insertMovieList(cachedMovies)
    }

    override suspend fun getAllMovies(): List<Movie> {
        return movieDao.getCachedMovieList().map { cachedMovies ->
            cachedMovies.map { it.toMovie() }
        }.first()
    }

    override suspend fun getMovieById(movieId: Int): Movie? {
        return movieDao.getCachedMovieById(movieId)?.toMovie()
    }

    override suspend fun clearAllMovies() {
        movieDao.clearAllMoviesCache()
    }
}

// Extension functions for mapping
fun Movie.toCachedMovie(): CachedMovie {
    return CachedMovie(
        id = this.id ?: 0,
        title = this.title ?: "",
        posterPath = this.posterPath ?: "",
        backdropPath = this.backdropPath,
        timestamp = System.currentTimeMillis()
    )
}

fun CachedMovie.toMovie(): Movie {
    return Movie(
        adult = false,
        backdropPath = this.backdropPath,
        genreIds = emptyList(),
        id = this.id,
        originalLanguage = "",
        originalTitle = this.title,
        overview = "",
        popularity = 0.0,
        posterPath = this.posterPath,
        releaseDate = "",
        title = this.title,
        video = false,
        voteAverage = 0.0,
        voteCount = 0 //
    )
}