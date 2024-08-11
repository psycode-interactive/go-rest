package com.psycodeinteractive.gorest.feature.users.presentation.userlist

import com.psycodeinteractive.gorest.feature.users.domain.usecase.DeleteUserUseCase
import com.psycodeinteractive.gorest.feature.users.domain.usecase.GetUsersUseCase
import com.psycodeinteractive.gorest.feature.users.presentation.model.ErrorMessage
import com.psycodeinteractive.gorest.feature.users.testutils.MainDispatcherRule
import com.psycodeinteractive.gorest.feature.users.testutils.fake.repository.TestUserRepository
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.domainUser1
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.domainUser2
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.presentationUser1
import com.psycodeinteractive.gorest.feature.users.testutils.fixture.presentationUser2
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var classUnderTest: UserListViewModel
    private lateinit var userRepository: TestUserRepository

    @Before
    fun setUp() {
        userRepository = TestUserRepository()
        getUsersUseCase = GetUsersUseCase(userRepository)
        deleteUserUseCase = DeleteUserUseCase(userRepository)
    }

    @Test
    fun `when users are fetched successfully, state is updated to Success`() = runTest {
        // Given
        val usersResponse = listOf(domainUser1, domainUser2)
        userRepository.testUsersResponse = Result.success(usersResponse)

        val expectedUsers = listOf(presentationUser1, presentationUser2)

        // When
        classUnderTest = UserListViewModel(getUsersUseCase, deleteUserUseCase)

        // Then
        classUnderTest.viewState.value shouldBe UserListViewState.Success(users = expectedUsers)
    }

    @Test
    fun `when users fetching fails, state is updated to Error`() = runTest {
        // Given
        userRepository.testUsersResponse = Result.failure(Exception("Fetching error"))

        // When
        classUnderTest = UserListViewModel(getUsersUseCase, deleteUserUseCase)

        // Then
        classUnderTest.viewState.value shouldBe UserListViewState.Error(
            errorMessage = ErrorMessage.Text("Fetching error")
        )
    }

    @Test
    fun `when add user action is triggered, state should update to navigate to add user`() = runTest { // When
        classUnderTest = UserListViewModel(getUsersUseCase, deleteUserUseCase)
        classUnderTest.onAddUserAction()

        // Then
        (classUnderTest.viewState.value as UserListViewState.Success).shouldNavigateToAddUser.shouldBeTrue()
    }

    @Test
    fun `when user long press is triggered, state should update with user for deletion`() = runTest {
        // Given
        userRepository.testUsersResponse = Result.success(listOf(domainUser1))

        // When
        classUnderTest = UserListViewModel(getUsersUseCase, deleteUserUseCase)
        classUnderTest.onUserLongPressAction(presentationUser1)

        // Then
        (classUnderTest.viewState.value as UserListViewState.Success).userForDeletion shouldBe UserForDeletion.User(presentationUser1)
    }

    @Test
    fun `when delete user action is triggered and succeeds, users should be refetched`() = runTest {
        // Given
        userRepository.testUsersResponse = Result.success(listOf(domainUser1, domainUser2))
        val expectedUsersAfterRefetch = listOf(presentationUser1)

        // When
        classUnderTest = UserListViewModel(getUsersUseCase, deleteUserUseCase)

        userRepository.testUsersResponse = Result.success(listOf(domainUser1))

        classUnderTest.onUserLongPressAction(presentationUser1)
        classUnderTest.onDeleteUserAction()

        // Then
        val state = classUnderTest.viewState.value as UserListViewState.Success
        state.isLoading.shouldBeFalse()
        state.users shouldBe expectedUsersAfterRefetch
    }

    @Test
    fun `when delete user action fails, state should show error`() = runTest {
        // Given
        userRepository.testUsersResponse = Result.success(listOf(domainUser1))
        userRepository.testDeleteUserResponse = Result.failure(Exception("Delete error"))

        // When
        classUnderTest = UserListViewModel(getUsersUseCase, deleteUserUseCase)
        classUnderTest.onUserLongPressAction(presentationUser1)
        classUnderTest.onDeleteUserAction()

        // Then
        val state = classUnderTest.viewState.value as UserListViewState.Success
        state.isLoading.shouldBeFalse()
        state.errorMessage shouldBe ErrorMessage.Text("Delete error")
    }

    @Test
    fun `when dismiss delete dialog is triggered, state should clear user for deletion`() = runTest {
        // Given
        userRepository.testUsersResponse = Result.success(listOf(domainUser1))

        // When
        classUnderTest = UserListViewModel(getUsersUseCase, deleteUserUseCase)
        classUnderTest.onUserLongPressAction(presentationUser1)
        classUnderTest.onDismissDeleteDialogAction()

        // Then
        (classUnderTest.viewState.value as UserListViewState.Success).userForDeletion shouldBe UserForDeletion.None
    }

    @Test
    fun `when navigation to add user is handled, state should reset navigation flag`() = runTest {
        // Given
        userRepository.testUsersResponse = Result.success(listOf(domainUser1))

        // When
        classUnderTest = UserListViewModel(getUsersUseCase, deleteUserUseCase)
        classUnderTest.onAddUserAction()

        classUnderTest.onNavigateToAddUserHandled()

        // Then
        (classUnderTest.viewState.value as UserListViewState.Success).shouldNavigateToAddUser.shouldBeFalse()
    }
}
