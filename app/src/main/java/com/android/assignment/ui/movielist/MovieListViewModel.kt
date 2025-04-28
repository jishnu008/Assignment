package com.android.assignment.ui.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.assignment.local.Dao.MovieDao

import com.android.assignment.model.Movie
import com.android.assignment.network.MovieApiService
import com.android.assignment.network.MoviePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieApiService: MovieApiService,
    private val movieDao: MovieDao,
    @Named("tmdb_api_key") private val apiKey: String
) : ViewModel() {

    val trendingMoviesFlow: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) {
        MoviePagingSource(movieApiService, apiKey, movieDao)
    }.flow.cachedIn(viewModelScope)

    private val _cachedTrendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val cachedTrendingMovies: StateFlow<List<Movie>> = _cachedTrendingMovies.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        loadCachedMovies()
    }

    private fun loadCachedMovies() {
        viewModelScope.launch {
            movieDao.getCachedMovieList().collectLatest { cachedMovieList ->
                _cachedTrendingMovies.value = cachedMovieList.map { it.toMovie() }
            }
        }
    }
}

// Extension function to map CachedMovie to Movie
fun com.android.assignment.local.entity.CachedMovie.toMovie(): Movie {
    return Movie(
        adult = false,
        backdropPath = backdropPath,
        genreIds = emptyList(),
        id = id,
        originalLanguage = "",
        originalTitle = title,
        overview = "",
        popularity = 0.0,
        posterPath = posterPath,
        releaseDate = "",
        title = title,
        video = false,
        voteAverage = 0.0,
        voteCount = 0
    )
}