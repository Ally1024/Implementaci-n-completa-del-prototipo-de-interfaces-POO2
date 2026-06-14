package com.example.avancesproyecto.data.repository

import com.example.avancesproyecto.data.local.dao.UserDao
import com.example.avancesproyecto.data.remote.UserApi
import com.example.avancesproyecto.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    suspend fun addUser(user: User) {
        val createdUser = api.createUser(user.toDto())
        dao.insertUser(createdUser.toEntity())
        syncUsers()
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