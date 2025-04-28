package com.android.assignment.ui.adduser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = findNavController()
                AddUserScreen(navController = navController)
            }
        }
    }
}

@Composable
fun AddUserScreen(navController: NavController ,  viewModel: AddUserViewModel = hiltViewModel()) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }

    val addUserResult by viewModel.addUserResult.collectAsStateWithLifecycle(initialValue = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Add New User", style = MaterialTheme.typography.bodyMedium)

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = jobTitle,
            onValueChange = { jobTitle = it },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.addUserOffline(firstName, lastName, email, jobTitle)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add User")
        }

        addUserResult?.let { result ->
            if (result.isSuccess) {
                Text("User added successfully!", color = MaterialTheme.colorScheme.secondary)
            } else if (result.isFailure) {
                Text("Failed to add user: ${result.exceptionOrNull()?.localizedMessage ?: "Unknown error"}", color = MaterialTheme.colorScheme.error)
            }
            LaunchedEffect(result) {
                // Optionally reset the state after a short delay
                kotlinx.coroutines.delay(2000)
                viewModel._addUserResult.value = null
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddUserScreenPreview() {
    MaterialTheme {
        // Provide a mock NavController for the preview
        val navController = rememberNavController()
        AddUserScreen(navController = navController)
    }
}