package com.android.assignment.ui.adduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.assignment.model.User
import com.android.assignment.model.UserEntity // Import UserEntity if your repo returns it
import com.android.assignment.repository.UserRepository
import com.android.assignment.utils.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    val _addUserResult = MutableStateFlow<Result<Long>?>(null)
    val addUserResult: StateFlow<Result<Long>?> = _addUserResult

    fun addUserOffline(firstName: String, lastName: String, email: String, jobTitle: String) {
        viewModelScope.launch {
            val offlineUser = User(
                firstName = firstName,
                lastName = lastName,
                email = email,
                jobTitle = jobTitle,
                isPendingSync = true
            )
            userRepository.addUserOffline(offlineUser)
                .collectLatest { result ->
                    _addUserResult.value = result
                }
        }


/*    fun addUser(firstName: String, lastName: String, email: String, jobTitle: String) {
        viewModelScope.launch {
            networkConnectivityObserver.observe().collectLatest { status ->
                val isOnline = status == NetworkConnectivityObserver.Status.Available
                val newUser = User(firstName = firstName, lastName = lastName, email = email, jobTitle = jobTitle)
                userRepository.addUser(newUser.firstName, newUser.lastName, newUser.jobTitle, isOnline).collectLatest { result ->
                    _addUserResult.value = result
                }
            }
        }*/
    }
}