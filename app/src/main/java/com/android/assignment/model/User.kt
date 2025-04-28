
package com.android.assignment.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "offline_users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    @SerializedName("id")
    val remoteId: Int? = null,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("avatar")
    val avatar: String? = null,
    val isPendingSync: Boolean = false,
    val jobTitle: String
)