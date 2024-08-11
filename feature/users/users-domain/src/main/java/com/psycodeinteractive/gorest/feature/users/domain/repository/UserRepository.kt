package com.psycodeinteractive.gorest.feature.users.domain.repository

import com.psycodeinteractive.gorest.feature.users.domain.model.CreateUserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.UserDomainModel

interface UserRepository {
    suspend fun getUsers(): Result<List<UserDomainModel>>
    suspend fun addUser(user: CreateUserDomainModel): Result<Unit>
    suspend fun deleteUser(id: Long): Result<Unit>
}