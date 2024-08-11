package com.psycodeinteractive.gorest.feature.users.testutils.fake.service

import com.psycodeinteractive.gorest.feature.users.data.source.remote.UserApiService
import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.CreateUserApiModel
import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.UserApiModel

const val testUserApiErrorMessage = "Test error message"

class TestUserApiService : UserApiService {
    var testUsersResponse: List<UserApiModel> = emptyList()
    var testAddUserResponse: Unit = Unit
    var testDeleteUserResponse: Unit = Unit

    var getUsersThrowError: Boolean = false
    var addUserThrowError: Boolean = false
    var deleteUserThrowError: Boolean = false

    override suspend fun fetchUsers(): List<UserApiModel> = if (getUsersThrowError) {
        throw Exception(testUserApiErrorMessage)
    } else {
        testUsersResponse
    }

    override suspend fun createUser(user: CreateUserApiModel) = if (addUserThrowError) {
        throw Exception(testUserApiErrorMessage)
    } else {
        testAddUserResponse
    }

    override suspend fun deleteUser(id: Long) = if (deleteUserThrowError) {
        throw Exception(testUserApiErrorMessage)
    } else {
        testDeleteUserResponse
    }
}
