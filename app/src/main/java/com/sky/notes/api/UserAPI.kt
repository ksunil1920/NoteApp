package com.sky.notes.api

import com.sky.notes.model.UserRequest
import com.sky.notes.model.UserResponse
import com.sky.notes.utils.NetworkResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {
    @POST("users/signup")
    suspend fun signup(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("users/signin")
    suspend fun signin(@Body userRequest: UserRequest): Response<UserResponse>
}