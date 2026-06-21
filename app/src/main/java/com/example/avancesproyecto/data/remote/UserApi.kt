package com.example.avancesproyecto.data.remote

import retrofit2.Response // <-- Asegúrate de tener este import nuevo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @GET("users")
    suspend fun getUsers(): List<UserDto>


    @POST("users")
    suspend fun createUser(@Body user: UserDto): Response<UserDto>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int)

    @PATCH("users/{id}/block")
    suspend fun toggleBlockUser(@Path("id") id: Int): UserDto
}