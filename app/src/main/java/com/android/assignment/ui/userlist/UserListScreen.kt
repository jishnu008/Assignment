package com.android.assignment.ui.userlist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.android.assignment.model.UserEntity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavController, viewModel: UserListViewModel = hiltViewModel()) {
    val users: LazyPagingItems<UserEntity> = viewModel.users.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("User List") })

        when (users.loadState.refresh) {
            is LoadState.Loading -> {
                // Initial Loading
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error -> {
                val e = users.loadState.refresh as LoadState.Error
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${e.error.localizedMessage}")
                }
            }
            is LoadState.NotLoading -> {
                // Initial load is complete, show the list
                if (users.itemCount > 0) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(count = users.itemCount) { index ->
                            val user = users[index]
                            if (user != null) {
                                UserListItem(user = user) { userId ->
                                    viewModel.onUserClicked(userId) // Call ViewModel if needed
                                    navController.navigate("movieList/$userId") // Navigate
                                }
                            }
                        }

                        // Handle append load state (loading more items)
                        item {
                            when (users.loadState.append) {
                                is LoadState.Loading -> {
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                }
                                is LoadState.Error -> {
                                    val e = users.loadState.append as LoadState.Error
                                    Text(text = "Error: ${e.error.localizedMessage}")
                                }
                                else -> {} // Do nothing if not loading or error
                            }
                        }
                    }
                } else {
                    // Empty list
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No users found")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserListItem(user: UserEntity, onUserClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val userId = user.remoteId ?: user.id
                Log.d("NavigationCheck", "Navigating to movieList/$userId")
                onUserClick(userId)
            }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            GlideImage(
                model = user.avatar,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "${user.firstName} ${user.lastName}", style = MaterialTheme.typography.titleMedium)
                if (user.jobTitle != null) {
                    Text(text = "Job: ${user.jobTitle}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}