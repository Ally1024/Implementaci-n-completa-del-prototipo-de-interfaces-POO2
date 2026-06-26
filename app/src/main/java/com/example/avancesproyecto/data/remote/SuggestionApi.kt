package com.example.avancesproyecto.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Esta interfaz define las peticiones HTTP para gestionar las propuestas de eventos desde la app móvil.
// Al igual que con los eventos, uso corrutinas (suspend) para que todo corra de fondo sin congelar la app.
interface SuggestionApi {

    // Petición GET para traerme todas las sugerencias hechas por los alumnos desde "/api/suggestions"
    @GET("suggestions")
    suspend fun getSuggestions(): List<SuggestionDto>

    // Petición POST para que un estudiante envíe una nueva propuesta al servidor desde su celular
    @POST("suggestions")
    suspend fun createSuggestion(
        @Body suggestion: SuggestionDto
    ): SuggestionDto

    // Petición POST clave: El administrador aprueba una sugerencia por su ID.
    // OJO: El servidor procesa la sugerencia y me responde con un 'EventDto' porque
    // en el backend la sugerencia se destruye y nace oficialmente un evento real.
    @POST("suggestions/{id}/approve")
    suspend fun approveSuggestion(
        @Path("id") id: Int
    ): EventDto

    // Petición DELETE para que el admin rechace o elimine una sugerencia directamente por su ID
    @DELETE("suggestions/{id}")
    suspend fun deleteSuggestion(
        @Path("id") id: Int
    )
}