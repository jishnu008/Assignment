package com.android.assignment.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_list")
data class CachedMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val backdropPath: String?,
    val timestamp: Long = System.currentTimeMillis()
)