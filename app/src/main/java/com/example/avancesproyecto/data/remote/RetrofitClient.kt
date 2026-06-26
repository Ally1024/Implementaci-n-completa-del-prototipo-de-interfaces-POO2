package com.example.avancesproyecto.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Uso un 'object' para crear un Singleton. Así me aseguro de tener una única instancia
// de Retrofit corriendo en toda la app y no consumir memoria de más.
object RetrofitClient {

    // Mi URL base apuntando al túnel de Ngrok.
    // Uso Ngrok porque me permite conectar el emulador o mi cel físico directamente a mi localhost de Spring Boot sin líos de IP.
    private const val BASE_URL = "https://expensive-backward-oncoming.ngrok-free.dev/api/"

    // Configuro un interceptor de logs para poder ver en la consola de Android Studio (Logcat)
    // exactamente qué JSON estoy mandando y qué me está respondiendo el servidor. El nivel BODY me muestra todo.
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // El cliente HTTP que va a llevar mi interceptor de logs integrado
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Configuración central de Retrofit usando inicialización perezosa (by lazy).
    // Solo se va a construir la primera vez que la app intente usar un API.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // Convierte automáticamente los JSON a mis Dtos de Kotlin usando Gson
            .build()
    }

    // Instancia perezosa para consumir los endpoints de Eventos
    val eventApi: EventApi by lazy {
        retrofit.create(EventApi::class.java)
    }

    // Instancia perezosa para consumir los endpoints de Usuarios (Login/Registro/Validación de CIF)
    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    // Instancia perezosa para consumir los endpoints de las Sugerencias que mandan los estudiantes
    val suggestionApi: SuggestionApi by lazy {
        retrofit.create(SuggestionApi::class.java)
    }
}