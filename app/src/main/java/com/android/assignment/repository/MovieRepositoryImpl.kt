package com.android.assignment.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.assignment.local.entity.CachedMovieDetail
import com.android.assignment.local.Dao.MovieDao
import com.android.assignment.local.Dao.MovieDetailDao

import com.android.assignment.model.Movie
import com.android.assignment.model.MovieDetailResponse
import com.android.assignment.network.MovieApiService
import com.android.assignment.network.MoviePagingSource
import com.android.assignment.utlis.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    @Named("tmdb_api_key") private val apiKey: String,
    private val movieDao: MovieDao,
    private val movieDetailDao: MovieDetailDao
) : MovieRepository {
    override fun getTrendingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                MoviePagingSource(movieApiService, apiKey, movieDao)
            }
        ).flow
    }

    override fun getCachedTrendingMovies(): Flow<List<Movie>> {
        return movieDao.getCachedMovieList().map { cachedMovies ->
            cachedMovies.map {
                Movie(
                    adult = false,
                    backdropPath = "",
                    genreIds = emptyList(),
                    id = it.id,
                    originalLanguage = "",
                    originalTitle = it.title,
                    overview = "",
                    popularity = 0.0,
                    posterPath = it.posterPath,
                    releaseDate = "",
                    title = it.title,
                    video = false,
                    voteAverage = 0.0,
                    voteCount = 0
                )
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Resource<MovieDetailResponse> {
        // Check local cache first
        val cachedDetail = movieDetailDao.getCachedMovieDetail(movieId)
        if (cachedDetail != null && cachedDetail.timestamp > System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24)) {
            return Resource.Success(cachedDetail.toMovieDetailResponse())
        }

        return try {
            val response = movieApiService.getMovieDetails(movieId, apiKey = apiKey)
            // Cache the fetched details
            withContext(Dispatchers.IO) {
                movieDetailDao.insertMovieDetail(CachedMovieDetail.fromMovieDetailResponse(response))
            }
            Resource.Success(response)
        } catch (e: Exception) {
            // If API call fails, return the cached data if available
            cachedDetail?.let { Resource.Success(it.toMovieDetailResponse()) }
                ?: Resource.Error("Error fetching movie details: ${e.localizedMessage}")
        }
    }

    override fun getCachedMovieDetail(movieId: Int): Flow<Resource<MovieDetailResponse>> {
        return movieDetailDao.getCachedMovieDetailFlow(movieId).map { cachedDetail ->
            cachedDetail?.toMovieDetailResponse()?.let { Resource.Success(it) }
                ?: Resource.Error("No cached movie detail found for ID: $movieId")
        }
    }
}

// Extension function to convert CachedMovieDetail to MovieDetailResponse
// In MovieRepositoryImpl.kt
fun CachedMovieDetail.toMovieDetailResponse(): MovieDetailResponse {
    return MovieDetailResponse(
        adult = adult,
        backdropPath = backdropPath,
        budget = budget,
        genres = genres,
        homepage = homepage,
        id = id,
        imdb_id = imdbId,
        original_language = originalLanguage,
        original_title = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        production_companies = productionCompanies,
        production_countries = productionCountries,
        release_date = releaseDate,
        revenue = revenue,
        runtime = runtime,
        spoken_languages = spokenLanguages,
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        vote_average = voteAverage,
        vote_count = voteCount
    )
}

fun CachedMovieDetail.Companion.fromMovieDetailResponse(response: com.android.assignment.model.MovieDetailResponse): CachedMovieDetail {
    return CachedMovieDetail(
        adult = response.adult ?: false,
        backdropPath = response.backdropPath.orEmpty(),
        budget = response.budget ?: 0,
        genres = response.genres ?: emptyList(),
        homepage = response.homepage.orEmpty(),
        id = response.id ?: 0,
        imdbId = response.imdb_id.orEmpty(),
        originalLanguage = response.original_language.orEmpty(),
        originalTitle = response.original_title.orEmpty(),
        overview = response.overview.orEmpty(),
        popularity = response.popularity ?: 0.0,
        posterPath = response.posterPath.orEmpty(),
        releaseDate = response.release_date.orEmpty(),
        revenue = response.revenue ?: 0,
        runtime = response.runtime ?: 0,
        status = response.status.orEmpty(),
        tagline = response.tagline.orEmpty(),
        title = response.title.orEmpty(),
        video = response.video ?: false,
        voteAverage = response.vote_average ?: 0.0,
        voteCount = response.vote_count ?: 0,
        timestamp = System.currentTimeMillis()

    )
}