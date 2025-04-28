package com.android.assignment.sync

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineSyncManager @Inject constructor(@ApplicationContext private val context: Context) {

    fun scheduleSync() {
        val syncWorkRequest = OneTimeWorkRequestBuilder<OfflineSyncWorker>()
            .build()
        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }
}