package com.android.assignment.ui.movielist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.assignment.model.Movie
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w92/" // Using w92 as per recent discussion

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MovieListScreen(
    navController: NavController,
    userId: Int?,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val movies: LazyPagingItems<Movie> = viewModel.trendingMoviesFlow.collectAsLazyPagingItems()
    val cachedMovies by viewModel.cachedTrendingMovies.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Trending Movies for User $userId") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (movies.loadState.refresh is LoadState.Loading && cachedMovies.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (movies.itemCount > 0) {
                // Display online data
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(count = movies.itemCount) { index ->
                        val movie = movies[index]
                        movie?.let {
                            MovieListItem(movie = it) { movieId ->
                                navController.navigate("movieDetail/$movieId")
                            }
                        }
                    }
                    item {
                        if (movies.loadState.append is LoadState.Loading) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        if (movies.loadState.append is LoadState.Error) {
                            val e = movies.loadState.append as LoadState.Error
                            Text(text = "Error loading more movies: ${e.error.localizedMessage}")
                        }
                    }
                }
            } else if (cachedMovies.isNotEmpty() && movies.loadState.refresh !is LoadState.Loading) {
                // Display cached data if available and not currently refreshing
                Text("Displaying cached movies...", style = MaterialTheme.typography.bodyMedium)
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(cachedMovies) { movie ->
                        MovieListItem(movie = movie) { movieId ->
                            navController.navigate("movieDetail/$movieId")
                        }
                    }
                }
            } else if (movies.loadState.refresh is LoadState.Error) {
                val e = movies.loadState.refresh as LoadState.Error
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error loading movies: ${e.error.localizedMessage}")
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No movies found (online or offline).")
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieListItem(movie: Movie, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { movie.id?.let(onItemClick) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium)
            ) {
                GlideImage(
                    model = "https://image.tmdb.org/t/p/w92/${movie.posterPath}",
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = movie.title ?: "No Title",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = movie.releaseDate ?: "No Release Date",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}