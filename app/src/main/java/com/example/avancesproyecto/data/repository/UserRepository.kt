package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.dao.UserDao
import com.example.avancesproyecto.data.remote.UserApi
import com.example.avancesproyecto.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response // <-- Importante este nuevo import

// Este repositorio gestiona el ciclo de vida de las cuentas de usuario en la app.
// Coordina la comunicación entre la API remota (UserApi) y el almacenamiento en Room (UserDao).
class UserRepository(
    private val api: UserApi,
    private val dao: UserDao
) {

    // Expongo el flujo reactivo de los usuarios locales convertidos a modelos limpios de dominio.
    // Útil para paneles de administración donde se listan o bloquean cuentas en tiempo real.
    val users: Flow<List<User>> = dao.getUsers()
        .map { list -> list.map { it.toModel() } }

    // Sincroniza la lista de usuarios: descarga las cuentas desde el servidor
    // y actualiza la caché de la base de datos local mediante Room.
    suspend fun syncUsers() {
        val remoteUsers = api.getUsers()
        dao.insertUsers(remoteUsers.map { it.toEntity() })
    }

    // Flujo crítico de Registro: Ahora retorno un Response<UserDto> envuelto.
    // Esto es vital porque si el backend arroja un error (por ejemplo, un código 400 Bad Request debido
    // a que el CIF de la UAM ya está registrado), el ViewModel podrá leerlo y avisarle al usuario en pantalla.
    suspend fun addUser(user: User): Response<com.example.avancesproyecto.data.remote.UserDto> {
        val response = api.createUser(user.toDto())

        // Estrategia de integridad: Solo si el servidor responde con un código de éxito (2xx),
        // procedo a guardar el usuario con su nuevo ID en la base de datos de Room y forzar un refresco.
        if (response.isSuccessful) {
            response.body()?.let { createdUser ->
                dao.insertUser(createdUser.toEntity())
                syncUsers() // Re-sincronizo para asegurar consistencia total
            }
        }

        return response
    }

    // Elimina una cuenta: impacta el backend para darlo de baja en Supabase
    // y lo purga inmediatamente de la tabla interna del teléfono.
    suspend fun deleteUser(userId: Int) {
        api.deleteUser(userId)
        dao.deleteUser(userId)
    }

    // Control de baneos/bloqueos: Invoca el endpoint PATCH del servidor para alternar el estado del usuario.
    // Al recibir el perfil modificado, actualiza Room e inicia una sincronización rápida para reflejar el cambio.
    suspend fun toggleBlockUser(userId: Int) {
        val updatedUser = api.toggleBlockUser(userId)
        dao.insertUser(updatedUser.toEntity())
        syncUsers()
    }
}