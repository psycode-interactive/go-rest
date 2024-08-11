package com.psycodeinteractive.gorest.feature.users.data.source.remote

import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.CreateUserApiModel
import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.UserApiModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {
    @GET("users")
    suspend fun fetchUsers(): List<UserApiModel>

    @POST("users")
    suspend fun createUser(@Body user: CreateUserApiModel)

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Long)
}
