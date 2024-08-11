package com.psycodeinteractive.gorest.feature.users.testutils.fake.repository

import com.psycodeinteractive.gorest.feature.users.domain.model.CreateUserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.UserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.repository.UserRepository

const val testErrorMessage = "Test error message"

class TestUserRepository : UserRepository {
    var testUsersResponse: Result<List<UserDomainModel>> = Result.success(emptyList())
    var testAddUserResponse: Result<Unit> = Result.success(Unit)
    var testDeleteUserResponse: Result<Unit> = Result.success(Unit)

    var getUsersThrowError: Boolean = false
    var addUserThrowError: Boolean = false
    var deleteUserThrowError: Boolean = false

    override suspend fun getUsers() = if (getUsersThrowError) {
        Result.failure(Exception(testErrorMessage))
    } else {
        testUsersResponse
    }

    override suspend fun addUser(user: CreateUserDomainModel): Result<Unit> = if (addUserThrowError) {
        Result.failure(Exception(testErrorMessage))
    } else {
        testAddUserResponse
    }

    override suspend fun deleteUser(id: Long): Result<Unit> = if (deleteUserThrowError) {
        Result.failure(Exception(testErrorMessage))
    } else {
        testDeleteUserResponse
    }
}
