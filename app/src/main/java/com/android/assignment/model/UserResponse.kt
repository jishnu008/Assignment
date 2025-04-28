package com.android.assignment.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val data: List<Data>,
    val support: Support
) {
    data class Data(
        val id: Int,
        val email: String,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("last_name")
        val lastName: String,
        val avatar: String
    )

    data class Support(
        val url: String,
        val text: String
    )
}