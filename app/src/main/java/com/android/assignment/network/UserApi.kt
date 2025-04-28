package com.android.assignment.network

import com.android.assignment.model.UserRequest
import com.android.assignment.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @GET("users")
    suspend fun getUsers(@Query("page") page: Int = 1): UserResponse

    @POST("users")
    suspend fun addUser(@Body userRequest: UserRequest): UserResponse
}