package com.android.assignment.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LifecycleCoroutineScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityObserver @Inject constructor(@ApplicationContext private val context: Context) {

    enum class Status {
        Available, Unavailable, Losing, Lost
    }

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline

    init {
        CoroutineScope(Dispatchers.IO).launch {
            observeStatus().collect { status ->
                _isOnline.value = status == Status.Available
            }
        }
    }

    fun observeStatus(): Flow<Status> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = android.net.NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                trySend(Status.Available).isSuccess
            }

            override fun onUnavailable() {
                trySend(Status.Unavailable).isSuccess
            }

            override fun onLosing(network: android.net.Network, lostReason: Int) {
                trySend(Status.Losing).isSuccess
            }

            override fun onLost(network: android.net.Network) {
                trySend(Status.Lost).isSuccess
            }
        }

        connectivityManager.registerNetworkCallback(request, networkCallback)

        trySend(getCurrentNetworkStatus(connectivityManager)).isSuccess

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.conflate()

    private fun getCurrentNetworkStatus(connectivityManager: ConnectivityManager): Status {
        val network = connectivityManager.activeNetwork ?: return Status.Unavailable
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return Status.Unavailable
        return if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) {
            Status.Available
        } else {
            Status.Unavailable
        }
    }
}