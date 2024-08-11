package com.psycodeinteractive.gorest.feature.users.presentation.adduser

import androidx.lifecycle.SavedStateHandle
import com.psycodeinteractive.gorest.feature.users.domain.usecase.AddUserUseCase
import com.psycodeinteractive.gorest.feature.users.presentation.R
import com.psycodeinteractive.gorest.feature.users.presentation.model.ErrorMessage
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.GenderPresentationModel
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.StatusPresentationModel
import com.psycodeinteractive.gorest.feature.users.testutils.MainDispatcherRule
import com.psycodeinteractive.gorest.feature.users.testutils.fake.repository.TestUserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AddUserViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var addUserUseCase: AddUserUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var userRepository: TestUserRepository
    private lateinit var classUnderTest: AddUserViewModel

    @Before
    fun setUp() {
        userRepository = TestUserRepository()
        addUserUseCase = AddUserUseCase(userRepository)
        savedStateHandle = SavedStateHandle()
        classUnderTest = AddUserViewModel(
            addUserUseCase = addUserUseCase,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `when user adds name, form is updated`() {
        // Given
        val newName = "John Doe"

        // When
        classUnderTest.onNameChanged(newName)

        // Then
        assertEquals(newName, classUnderTest.viewState.value.formData.name)
        assertTrue(classUnderTest.viewState.value.formValidation.isNameEntered)
    }

    @Test
    fun `when user adds an invalid email, form shows email as invalid`() {
        // Given
        val invalidEmail = "john.doe"

        // When
        classUnderTest.onEmailChanged(invalidEmail)

        // Then
        assertEquals(invalidEmail, classUnderTest.viewState.value.formData.email)
        assertFalse(classUnderTest.viewState.value.formValidation.isEmailValid)
    }

    @Test
    fun `when user adds a valid email, form shows email as valid`() {
        // Given
        val validEmail = "john.doe@example.com"

        // When
        classUnderTest.onEmailChanged(validEmail)

        // Then
        assertEquals(validEmail, classUnderTest.viewState.value.formData.email)
        assertTrue(classUnderTest.viewState.value.formValidation.isEmailValid)
    }

    @Test
    fun `when user selects gender, form is updated`() {
        // Given
        val selectedGender = GenderPresentationModel.Male

        // When
        classUnderTest.onGenderSelected(selectedGender)

        // Then
        assertEquals(selectedGender, classUnderTest.viewState.value.formData.gender)
    }

    @Test
    fun `when user selects status, form is updated`() {
        // Given
        val selectedStatus = StatusPresentationModel.Inactive

        // When
        classUnderTest.onStatusSelected(selectedStatus)

        // Then
        assertEquals(selectedStatus, classUnderTest.viewState.value.formData.status)
    }

    @Test
    fun `when user clicks add, loading is shown and user is added successfully`() = runTest {
        // Given
        val name = "John Doe"
        val email = "john.doe@example.com"
        val gender = GenderPresentationModel.Male
        val status = StatusPresentationModel.Active

        userRepository.testAddUserResponse = Result.success(Unit)

        classUnderTest.onNameChanged(name)
        classUnderTest.onEmailChanged(email)
        classUnderTest.onGenderSelected(gender)
        classUnderTest.onStatusSelected(status)

        // When
        classUnderTest.onAddUserAction()

        // Then
        assertFalse(classUnderTest.viewState.value.isLoading)
        assertTrue(classUnderTest.viewState.value.wasUserAdded)
        assertTrue(classUnderTest.viewState.value.isFlowFinished)
        assertNull(classUnderTest.viewState.value.errorMessage)
    }

    @Test
    fun `when user clicks add and adding fails, error is shown`() = runTest {
        // Given
        val name = "John Doe"
        val email = "john.doe@example.com"
        val gender = GenderPresentationModel.Male
        val status = StatusPresentationModel.Active

        userRepository.addUserThrowError = true


        classUnderTest.onNameChanged(name)
        classUnderTest.onEmailChanged(email)
        classUnderTest.onGenderSelected(gender)
        classUnderTest.onStatusSelected(status)

        // When
        classUnderTest.onAddUserAction()

        // Then
        assertFalse(classUnderTest.viewState.value.isLoading)
        assertFalse(classUnderTest.viewState.value.wasUserAdded)
        assertTrue(classUnderTest.viewState.value.errorMessage is ErrorMessage.Resource)
        assertEquals(R.string.error_add_user, (classUnderTest.viewState.value.errorMessage as ErrorMessage.Resource).messageResource)
    }

    @Test
    fun `when user cancels, flow is finished`() {
        // When
        classUnderTest.onCancelAction()

        // Then
        assertTrue(classUnderTest.viewState.value.isFlowFinished)
    }

    @Test
    fun `when user adds empty name, form shows name as not entered`() {
        // Given
        val emptyName = ""

        // When
        classUnderTest.onNameChanged(emptyName)

        // Then
        assertEquals(emptyName, classUnderTest.viewState.value.formData.name)
        assertFalse(classUnderTest.viewState.value.formValidation.isNameEntered)
    }

    @Test
    fun `when user adds invalid email, form is invalid`() {
        // Given
        val invalidEmail = "invalid_email"

        // When
        classUnderTest.onEmailChanged(invalidEmail)

        // Then
        assertFalse(classUnderTest.viewState.value.formValidation.isFormValid)
    }

    @Test
    fun `when user adds valid email but empty name, form is invalid`() {
        // Given
        val validEmail = "john.doe@example.com"
        val emptyName = ""

        // When
        classUnderTest.onEmailChanged(validEmail)
        classUnderTest.onNameChanged(emptyName)

        // Then
        assertFalse(classUnderTest.viewState.value.formValidation.isFormValid)
    }

    @Test
    fun `when user adds valid email and name, form is valid`() {
        // Given
        val validEmail = "john.doe@example.com"
        val validName = "John Doe"

        // When
        classUnderTest.onEmailChanged(validEmail)
        classUnderTest.onNameChanged(validName)

        // Then
        assertTrue(classUnderTest.viewState.value.formValidation.isFormValid)
    }
}
