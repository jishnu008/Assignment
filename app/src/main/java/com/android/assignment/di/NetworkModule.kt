package com.android.assignment.di

import com.android.assignment.BuildConfig
import com.android.assignment.network.ApiKeyInterceptor
import com.android.assignment.network.MovieApiService
import com.android.assignment.network.UserApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val USER_BASE_URL = "https://reqres.in/api/"
    private const val MOVIE_BASE_URL = "https://api.themoviedb.org/3/"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    @MovieRetrofit
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @UserRetrofit
    fun provideUserOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @UserRetrofit
    fun provideUserRetrofit(gson: Gson, @UserRetrofit userOkHttpClient: OkHttpClient): Retrofit { // Use qualified OkHttpClient
        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(userOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @MovieRetrofit
    fun provideMovieRetrofit(gson: Gson, @MovieRetrofit okHttpClient: OkHttpClient): Retrofit { // Use qualified OkHttpClient
        return Retrofit.Builder()
            .baseUrl(MOVIE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(@UserRetrofit userRetrofit: Retrofit): UserApi {
        return userRetrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieApiService(@MovieRetrofit movieRetrofit: Retrofit): MovieApiService {
        return movieRetrofit.create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("tmdb_api_key") // Add this annotation
    fun provideApiKey(): String {
        return BuildConfig.TMDB_API_KEY
    }
}