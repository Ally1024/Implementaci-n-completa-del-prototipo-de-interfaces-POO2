package com.example.avancesproyecto.data.remote

import retrofit2.Response // <-- Asegúrate de tener este import nuevo
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

// Esta interfaz define las peticiones HTTP para controlar todo lo relacionado con los usuarios de la app.
// Uso corrutinas (suspend) para que los flujos de login, registro y baneo no congelen la interfaz gráfica.
interface UserApi {

    // Petición GET para obtener la lista de todos los usuarios registrados en el sistema
    @GET("users")
    suspend fun getUsers(): List<UserDto>

    // Petición POST para registrar a un nuevo usuario.
    // OJO: Aquí devuelvo un objeto 'Response<UserDto>' envuelto, porque necesito validar en el frontend
    // si el servidor responde con un código de error (como un 400 BadRequest) cuando el CIF o el nombre ya existen.
    @POST("users")
    suspend fun createUser(@Body user: UserDto): Response<UserDto>

    // Petición DELETE para eliminar de forma definitiva la cuenta de un usuario mediante su ID
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int)

    // Petición PATCH para activar o desactivar el baneo/bloqueo de un usuario específico por su ID.
    // Uso PATCH porque solo voy a modificar un atributo en específico (el estado lógico de bloqueo).
    @PATCH("users/{id}/block")
    suspend fun toggleBlockUser(@Path("id") id: Int): UserDto
}