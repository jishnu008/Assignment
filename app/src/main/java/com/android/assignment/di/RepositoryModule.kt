package com.android.assignment.di

import com.android.assignment.local.Dao.MovieDao
import com.android.assignment.local.Dao.MovieDetailDao
import com.android.assignment.network.MovieApiService
import com.android.assignment.repository.MovieRepository
import com.android.assignment.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieApiService: MovieApiService,
        @Named("tmdb_api_key") apiKey: String,
        movieDao: MovieDao,
        movieDetailDao: MovieDetailDao
    ): MovieRepository {
        return MovieRepositoryImpl(movieApiService, apiKey, movieDao, movieDetailDao)
    }
}