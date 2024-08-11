package com.psycodeinteractive.gorest.feature.users.data

import com.psycodeinteractive.gorest.feature.users.data.repository.UserDataRepository
import com.psycodeinteractive.gorest.feature.users.domain.model.CreateUserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.GenderDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.StatusDomainModel
import com.psycodeinteractive.gorest.feature.users.testutils.fake.service.TestUserApiService
import com.psycodeinteractive.gorest.feature.users.testutils.fake.service.testUserApiErrorMessage
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.apiUser1
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.apiUser2
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.domainUser1
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.domainUser2
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserDataRepositoryTest {

    private lateinit var fakeUserApiService: TestUserApiService
    private lateinit var classUnderTest: UserDataRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        fakeUserApiService = TestUserApiService()
        classUnderTest = UserDataRepository(fakeUserApiService, testDispatcher)
    }

    @Test
    fun `getUsers returns list of users when there are users`() = runTest(testDispatcher) {
        // Given
        fakeUserApiService.testUsersResponse = listOf(apiUser1, apiUser2)
        val expectedUsers = listOf(domainUser1, domainUser2)

        // When
        val result = classUnderTest.getUsers().getOrThrow()

        // Then
        result shouldBe expectedUsers
    }

    @Test
    fun `getUsers returns empty list when there are no users`() = runTest(testDispatcher) {
        // Given
        // When
        val result = classUnderTest.getUsers().getOrThrow()

        // Then
        result shouldBe emptyList()
    }

    @Test
    fun `getUsers returns error when API returns failure`() = runTest(testDispatcher) {
        // Given
        fakeUserApiService.getUsersThrowError = true

        // When
        val result = classUnderTest.getUsers()

        // Then
        result.isFailure.shouldBeTrue()
        result.exceptionOrNull()?.message shouldBe testUserApiErrorMessage
    }

    @Test
    fun `addUser adds a new user successfully`() = runTest(testDispatcher) {
        // Given
        val createUserDomainModel = CreateUserDomainModel(name = "John Doe", email = "john@example.com", gender = GenderDomainModel.Male, status = StatusDomainModel.Active)

        // When
        val result = classUnderTest.addUser(createUserDomainModel)

        // Then
        result.isSuccess.shouldBeTrue()
    }

    @Test
    fun `addUser returns error when API returns failure`() = runTest(testDispatcher) {
        // Given
        val createUserDomainModel = CreateUserDomainModel(name = "John Doe", email = "john@example.com", gender = GenderDomainModel.Male, status = StatusDomainModel.Active)
        fakeUserApiService.addUserThrowError = true

        // When
        val result = classUnderTest.addUser(createUserDomainModel)

        // Then
        result.isFailure.shouldBeTrue()
        result.exceptionOrNull()?.message shouldBe testUserApiErrorMessage
    }

    @Test
    fun `deleteUser removes the user successfully`() = runTest(testDispatcher) {
        // When
        val result = classUnderTest.deleteUser(0)

        // Then
        result.isSuccess.shouldBeTrue()
    }

    @Test
    fun `deleteUser returns error when API returns failure`() = runTest(testDispatcher) {
        // Given
        fakeUserApiService.deleteUserThrowError = true

        // When
        val result = classUnderTest.deleteUser(1)

        // Then
        result.isFailure.shouldBeTrue()
        result.exceptionOrNull()?.message shouldBe testUserApiErrorMessage
    }
}
