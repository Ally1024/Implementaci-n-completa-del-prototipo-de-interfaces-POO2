package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.dao.UserDao
import com.example.avancesproyecto.data.remote.UserApi
import com.example.avancesproyecto.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response // <-- Importante este nuevo import

class UserRepository(
    private val api: UserApi,
    private val dao: UserDao
) {

    val users: Flow<List<User>> = dao.getUsers()
        .map { list -> list.map { it.toModel() } }

    suspend fun syncUsers() {
        val remoteUsers = api.getUsers()
        dao.insertUsers(remoteUsers.map { it.toEntity() })
    }

    // 🛠️ MODIFICADO: Ahora retorna un Response<UserDto> para que el ViewModel evalúe si hubo duplicados
    suspend fun addUser(user: User): Response<com.example.avancesproyecto.data.remote.UserDto> {
        val response = api.createUser(user.toDto())

        // Solo si el servidor lo guardó con éxito en Supabase, lo agregamos a la BD local
        if (response.isSuccessful) {
            response.body()?.let { createdUser ->
                dao.insertUser(createdUser.toEntity())
                syncUsers()
            }
        }

        return response
    }

    suspend fun deleteUser(userId: Int) {
        api.deleteUser(userId)
        dao.deleteUser(userId)
    }

    suspend fun toggleBlockUser(userId: Int) {
        val updatedUser = api.toggleBlockUser(userId)
        dao.insertUser(updatedUser.toEntity())
        syncUsers()
    }
}