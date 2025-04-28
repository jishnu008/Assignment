package com.android.assignment.model

import com.google.gson.annotations.SerializedName

data class UserRequest(
    val name: String,
    val job: String
)