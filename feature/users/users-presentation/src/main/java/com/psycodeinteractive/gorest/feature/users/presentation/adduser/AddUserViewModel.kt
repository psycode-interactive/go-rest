package com.psycodeinteractive.gorest.feature.users.presentation.adduser

import android.os.Parcelable
import androidx.compose.runtime.saveable.Saver
import androidx.core.util.PatternsCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.psycodeinteractive.gorest.core.presentation.BaseViewModel
import com.psycodeinteractive.gorest.core.presentation.ViewState
import com.psycodeinteractive.gorest.feature.users.domain.model.CreateUserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.usecase.AddUserUseCase
import com.psycodeinteractive.gorest.feature.users.presentation.R
import com.psycodeinteractive.gorest.feature.users.presentation.model.ErrorMessage
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.GenderPresentationModel
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.StatusPresentationModel
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<AddUserViewState>() {
    override val initialViewState = run {
        val formData = savedStateHandle.saveable(
            key = formDataKey,
            init = ::FormData,
            saver = Saver(
                save = { viewState.value.formData },
                restore = { it }
            )
        )
        AddUserViewState(
            formData = formData,
            formValidation = validateForm(formData)
        )
    }

    fun onAddUserAction() {
        updateViewState { lastState ->
            lastState.copy(isLoading = true)
        }
        val user = viewState.value.formData.run {
            CreateUserDomainModel(
                name = name,
                email = email,
                gender = gender.toDomain(),
                status = status.toDomain()
            )
        }
        viewModelScope.launch {
            addUserUseCase(user)
                .onSuccess {
                    updateViewState { lastState ->
                        lastState.copy(
                            isLoading = false,
                            wasUserAdded = true,
                            isFlowFinished = true
                        )
                    }
                }
                .onFailure { error ->
                    updateViewState { lastState ->
                        lastState.copy(
                            isLoading = false,
                            errorMessage = error.message?.let(ErrorMessage::Text)
                                ?: ErrorMessage.Resource(R.string.error_add_user)
                        )
                    }
                }
        }
    }

    fun onNameChanged(name: String) {
        updateViewState { lastState ->
            lastState.copy(
                formData = lastState.formData.copy(name = name),
                formValidation = lastState.formValidation.copy(
                    isNameEntered = name.isNotBlank()
                )
            )
        }
    }

    fun onEmailChanged(email: String) {
        updateViewState { lastState ->
            lastState.copy(
                formData = lastState.formData.copy(email = email),
                formValidation = lastState.formValidation.copy(
                    isEmailValid = isEmailValid(email)
                )
            )
        }
    }

    fun onGenderSelected(gender: GenderPresentationModel) {
        updateViewState { lastState ->
            lastState.copy(
                formData = lastState.formData.copy(gender = gender),
            )
        }
    }

    fun onStatusSelected(status: StatusPresentationModel) {
        updateViewState { lastState ->
            lastState.copy(
                formData = lastState.formData.copy(status = status),
            )
        }
    }

    fun onCancelAction() {
        updateViewState { lastState ->
            lastState.copy(isFlowFinished = true)
        }
    }

    private fun validateForm(formData: FormData) = FormValidation(
        isEmailValid = isEmailValid(formData.email),
        isNameEntered = formData.name.isNotBlank()
    )

    private fun isEmailValid(email: String) =
        PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
}

data class AddUserViewState(
    val isLoading: Boolean = false,
    val errorMessage: ErrorMessage? = null,
    val wasUserAdded: Boolean = false,
    val isFlowFinished: Boolean = false,
    val formValidation: FormValidation = FormValidation(),
    val formData: FormData = FormData(),
    val genderOptions: List<GenderPresentationModel> = GenderPresentationModel.entries,
    val statusOptions: List<StatusPresentationModel> = StatusPresentationModel.entries,
) : ViewState

@Parcelize
data class FormData(
    val name: String = "",
    val email: String = "",
    val gender: GenderPresentationModel = GenderPresentationModel.Female,
    val status: StatusPresentationModel = StatusPresentationModel.Active,
) : Parcelable

data class FormValidation(
    val isEmailValid: Boolean = false,
    val isNameEntered: Boolean = false,
) {
    val isFormValid: Boolean
        get() = isEmailValid && isNameEntered
}

private const val formDataKey = "formData"
