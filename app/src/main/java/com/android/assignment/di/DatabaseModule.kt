package com.android.assignment.di

import android.content.Context
import androidx.room.Room
import com.android.assignment.local.AppDatabase
import com.android.assignment.local.Dao.MovieDao
import com.android.assignment.local.Dao.MovieDetailDao // Import MovieDetailDao
import com.android.assignment.local.MovieLocalDataSource
import com.android.assignment.local.MovieLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "assignment_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    fun provideOfflineUserDao(appDatabase: AppDatabase) = appDatabase.offlineUserDao()

    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Provides
    fun provideMovieDetailDao(appDatabase: AppDatabase): MovieDetailDao {
        return appDatabase.movieDetailDao()
    }

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(movieDao: MovieDao): MovieLocalDataSource {
        return MovieLocalDataSourceImpl(movieDao)
    }
}