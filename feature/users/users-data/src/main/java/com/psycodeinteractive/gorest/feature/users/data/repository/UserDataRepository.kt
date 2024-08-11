package com.psycodeinteractive.gorest.feature.users.data.repository

import com.psycodeinteractive.gorest.feature.users.data.mappers.toApi
import com.psycodeinteractive.gorest.feature.users.data.mappers.toDomain
import com.psycodeinteractive.gorest.feature.users.data.source.remote.UserApiService
import com.psycodeinteractive.gorest.feature.users.domain.model.CreateUserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun getUsers() = withContext(ioDispatcher) {
        Result.runCatching {
            userApiService.fetchUsers().map { it.toDomain() }
        }
    }

    override suspend fun addUser(user: CreateUserDomainModel) = withContext(ioDispatcher) {
        Result.runCatching {
            userApiService.createUser(user.toApi())
        }
    }

    override suspend fun deleteUser(id: Long) = withContext(ioDispatcher) {
        Result.runCatching {
            userApiService.deleteUser(id)
        }
    }
}
