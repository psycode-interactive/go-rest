package com.psycodeinteractive.gorest.feature.users.domain.usecase

import com.psycodeinteractive.gorest.feature.users.testutils.MainDispatcherRule
import com.psycodeinteractive.gorest.feature.users.testutils.fake.repository.TestUserRepository
import com.psycodeinteractive.gorest.feature.users.testutils.fake.repository.testUserRepositoryErrorMessage
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DeleteUserUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var classUnderTest: DeleteUserUseCase
    private lateinit var userRepository: TestUserRepository

    @Before
    fun setUp() {
        userRepository = TestUserRepository()
        classUnderTest = DeleteUserUseCase(userRepository)
    }

    @Test
    fun `invoke returns success when user is added successfully`() = runTest {
        // Given
        userRepository.testDeleteUserResponse = Result.success(Unit)

        // When
        val result = classUnderTest(0)

        // Then
        result.isSuccess.shouldBeTrue()
    }

    @Test
    fun `invoke returns error when repository returns failure`() = runTest {
        // Given
        userRepository.deleteUserThrowError = true

        // When
        val result = classUnderTest(0)

        // Then
        result.isFailure.shouldBeTrue()
        result.exceptionOrNull()?.message.shouldBe(testUserRepositoryErrorMessage)
    }
}
