package com.android.assignment.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_entity")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteId: Int? = null,
    val firstName: String,
    val lastName: String,
    val avatar: String,
    val jobTitle: String?,
    val isPendingSync: Boolean = false
)