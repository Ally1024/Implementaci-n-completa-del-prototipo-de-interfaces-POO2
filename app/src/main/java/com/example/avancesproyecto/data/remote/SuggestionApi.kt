package com.example.avancesproyecto.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SuggestionApi {

    @GET("suggestions")
    suspend fun getSuggestions(): List<SuggestionDto>

    @POST("suggestions")
    suspend fun createSuggestion(
        @Body suggestion: SuggestionDto
    ): SuggestionDto

    @POST("suggestions/{id}/approve")
    suspend fun approveSuggestion(
        @Path("id") id: Int
    ): EventDto

    @DELETE("suggestions/{id}")
    suspend fun deleteSuggestion(
        @Path("id") id: Int
    )
}