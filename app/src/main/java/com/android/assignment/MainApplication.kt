package com.android.assignment

import android.app.Application
import androidx.room.Room
import com.android.assignment.local.AppDatabase
import com.android.assignment.local.MIGRATION_3_4

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "assignment_database"
        )
            .addMigrations(MIGRATION_3_4)
            .build()
    }
    fun getAppDatabase(): AppDatabase = database
}