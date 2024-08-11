package com.psycodeinteractive.gorest.feature.users.domain.usecase

import com.psycodeinteractive.gorest.feature.users.testutils.MainDispatcherRule
import com.psycodeinteractive.gorest.feature.users.testutils.fake.repository.TestUserRepository
import com.psycodeinteractive.gorest.feature.users.testutils.fake.repository.testUserRepositoryErrorMessage
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.domainUser1
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.domainUser2
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetUsersUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var classUnderTest: GetUsersUseCase
    private lateinit var userRepository: TestUserRepository

    @Before
    fun setUp() {
        userRepository = TestUserRepository()
        classUnderTest = GetUsersUseCase(userRepository)
    }

    @Test
    fun `invoke returns success when user is added successfully`() = runTest {
        // Given
        val users = listOf(
            domainUser1,
            domainUser2
        )
        userRepository.testGetUsersResponse = Result.success(users)

        // When
        val result = classUnderTest()

        // Then
        result.isSuccess.shouldBeTrue()
        result.getOrNull() shouldBe users
    }

    @Test
    fun `invoke returns error when repository returns failure`() = runTest {
        // Given
        userRepository.getUsersThrowError = true

        // When
        val result = classUnderTest()

        // Then
        result.isFailure.shouldBeTrue()
        result.exceptionOrNull()?.message shouldBe testUserRepositoryErrorMessage
    }
}
