package com.android.assignment.ui.moviedetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.android.assignment.model.MovieDetailResponse

import com.android.assignment.utlis.Resource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailScreen(
    navController: NavController,
    movieId: Int,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val movieDetailResource by viewModel.movieDetails.collectAsState(initial = Resource.Loading())

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Details") },
                navigationIcon = {

                    /* IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    } */
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (movieDetailResource) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Success -> {
                    val movieDetail = (movieDetailResource as Resource.Success<MovieDetailResponse>).data
                    if (movieDetail != null) {
                        GlideImage(
                            model = "https://image.tmdb.org/t/p/w500/${movieDetail.posterPath}",
                            contentDescription = "Movie Poster",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        movieDetail.title?.let { Text(it, style = MaterialTheme.typography.headlineMedium) }
                        Spacer(modifier = Modifier.height(8.dp))
                        movieDetail.release_date?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                        Spacer(modifier = Modifier.height(16.dp))
                        movieDetail.overview?.let { Text(it, style = MaterialTheme.typography.bodyLarge) }
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Text("Movie details not found.")
                    }
                }
                is Resource.Error<*> -> {
                    Text("Error loading movie details: ${(movieDetailResource as Resource.Error<*>).message}")
                }
            }
        }
    }
}