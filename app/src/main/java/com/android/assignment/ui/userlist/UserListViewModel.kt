package com.android.assignment.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.PagingData
import com.android.assignment.model.UserEntity
import com.android.assignment.repository.UserRepository
import com.android.assignment.utils.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _users: MutableStateFlow<PagingData<UserEntity>> = MutableStateFlow(PagingData.empty())
    val users: StateFlow<PagingData<UserEntity>> = _users

    private val _selectedUserId = MutableStateFlow<Int?>(null)
    val selectedUserId: StateFlow<Int?> = _selectedUserId

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            networkConnectivityObserver.isOnline.collectLatest { isOnline ->
                Pager(
                    config = PagingConfig(
                        pageSize = 20,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = { userRepository.getUsersStream(isOnline) }
                ).flow.cachedIn(viewModelScope).collectLatest { pagingData ->
                    _users.value = pagingData
                }
            }
        }
    }

    fun onUserClicked(userId: Int) {
        _selectedUserId.value = userId
        println("User clicked with ID: $userId")
    }
}