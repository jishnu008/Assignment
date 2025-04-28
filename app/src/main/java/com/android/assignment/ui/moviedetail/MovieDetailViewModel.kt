package com.android.assignment.ui.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.assignment.model.MovieDetailResponse
import com.android.assignment.repository.MovieRepository
import com.android.assignment.utlis.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movieDetails = MutableStateFlow<Resource<MovieDetailResponse>>(Resource.Loading())
    val movieDetails: StateFlow<Resource<MovieDetailResponse>> = _movieDetails

    fun getMovieDetails(movieId: Int) {
        _movieDetails.value = Resource.Loading()
        viewModelScope.launch {
            val result = movieRepository.getMovieDetails(movieId)
            _movieDetails.value = result
        }
    }

}