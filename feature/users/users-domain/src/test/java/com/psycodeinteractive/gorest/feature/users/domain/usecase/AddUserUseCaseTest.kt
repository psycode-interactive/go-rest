package com.psycodeinteractive.gorest.feature.users.domain.usecase

import com.psycodeinteractive.gorest.feature.users.domain.model.CreateUserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.GenderDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.StatusDomainModel
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
class AddUserUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var classUnderTest: AddUserUseCase
    private lateinit var userRepository: TestUserRepository

    @Before
    fun setUp() {
        userRepository = TestUserRepository()
        classUnderTest = AddUserUseCase(userRepository)
    }

    @Test
    fun `invoke returns success when user is added successfully`() = runTest {
        // Given
        val createUserDomainModel = CreateUserDomainModel(
            name = "John Doe",
            email = "john@example.com",
            gender = GenderDomainModel.Male,
            status = StatusDomainModel.Active
        )
        userRepository.testAddUserResponse = Result.success(Unit)

        // When
        val result = classUnderTest(createUserDomainModel)

        // Then
        result.isSuccess.shouldBeTrue()
    }

    @Test
    fun `invoke returns error when repository returns failure`() = runTest {
        // Given
        val createUserDomainModel = CreateUserDomainModel(
            name = "John Doe",
            email = "john@example.com",
            gender = GenderDomainModel.Male,
            status = StatusDomainModel.Active
        )
        userRepository.addUserThrowError = true

        // When
        val result = classUnderTest(createUserDomainModel)

        // Then
        result.isFailure.shouldBeTrue()
        result.exceptionOrNull()?.message.shouldBe(testUserRepositoryErrorMessage)
    }
}
