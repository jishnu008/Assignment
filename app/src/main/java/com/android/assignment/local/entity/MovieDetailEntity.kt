package com.android.assignment.local.entity



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetailEntity(
    @PrimaryKey val id: Int,
    val title: String?,
    val overview: String?,
    val releaseDate: String?,
    val runtime: Int?,
    val voteAverage: Double?,
    val backdropPath: String?,
    val timestamp: Long,

)