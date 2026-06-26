package com.example.avancesproyecto.data.remote

import retrofit2.http.*

// Esta interfaz define todas las peticiones HTTP que mi app de Android le va a hacer a mi API de Spring Boot.
// Uso Retrofit para que maneje la comunicación y 'suspend' para que las peticiones corran en hilos secundarios
// mediante corrutinas, evitando que la pantalla del celular se congele.
interface EventApi {

    // Petición GET para traerme la lista completa de eventos desde "/api/events"
    @GET("events")
    suspend fun getEvents(): List<EventDto>

    // Petición GET para buscar un evento específico usando su ID en la URL
    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: Int): EventDto

    // Petición POST para mandarle al servidor un nuevo evento adentro del cuerpo (Body) de la solicitud
    @POST("events")
    suspend fun createEvent(@Body event: EventDto): EventDto

    // Petición PUT para actualizar un evento existente mandando su ID en la URL y los nuevos datos en el Body
    @PUT("events/{id}")
    suspend fun updateEvent(
        @Path("id") id: Int,
        @Body event: EventDto
    ): EventDto

    // Petición DELETE para borrar un evento de la base de datos usando su ID
    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") id: Int)

    // Petición POST especial para simular la acción de unirme a un evento incrementando los asistentes
    @POST("events/{id}/join")
    suspend fun joinEvent(@Path("id") id: Int): EventDto
}