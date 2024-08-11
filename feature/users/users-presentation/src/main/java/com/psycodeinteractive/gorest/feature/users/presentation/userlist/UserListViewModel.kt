package com.psycodeinteractive.gorest.feature.users.presentation.userlist

import androidx.lifecycle.viewModelScope
import com.psycodeinteractive.gorest.core.presentation.BaseViewModel
import com.psycodeinteractive.gorest.core.presentation.ViewState
import com.psycodeinteractive.gorest.feature.users.domain.usecase.DeleteUserUseCase
import com.psycodeinteractive.gorest.feature.users.domain.usecase.GetUsersUseCase
import com.psycodeinteractive.gorest.feature.users.presentation.R
import com.psycodeinteractive.gorest.feature.users.presentation.model.ErrorMessage
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.UserPresentationModel
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) : BaseViewModel<UserListViewState>() {
    override val initialViewState = UserListViewState.Initializing

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        updateViewState { lastState ->
            when (lastState) {
                is UserListViewState.Success -> lastState.copy(isLoading = true)
                else -> lastState
            }
        }
        viewModelScope.launch {
            getUsersUseCase()
                .onSuccess { fetchedUsers ->
                    val users = fetchedUsers.map { it.toPresentation() }
                    updateViewState { lastState ->
                        when (lastState) {
                            is UserListViewState.Success -> lastState.copy(
                                users = users,
                                isLoading = false,
                                errorMessage = null
                            )
                            else -> UserListViewState.Success(
                                users = users,
                            )
                        }
                    }
                }
                .onFailure { error ->
                    updateViewState { lastState ->
                        val errorMessage = error.message?.let(ErrorMessage::Text)
                            ?: ErrorMessage.Resource(R.string.error_get_users)
                        when (lastState) {
                            is UserListViewState.Success -> lastState.copy(
                                errorMessage = errorMessage,
                                isLoading = false
                            )
                            else -> UserListViewState.Error(errorMessage)
                        }
                    }
                }
        }
    }

    fun onTryAgainAction() {
        fetchUsers()
    }

    fun onUserWasAdded() {
        fetchUsers()
    }

    fun onAddUserAction() {
        if (viewState.value !is UserListViewState.Success) return
        updateViewState { lastState ->
            lastState as UserListViewState.Success
            lastState.copy(shouldNavigateToAddUser = true)
        }
    }

    fun onUserLongPressAction(
        user: UserPresentationModel
    ) {
        updateViewState { lastState ->
            lastState as UserListViewState.Success
            lastState.copy(userForDeletion = UserForDeletion.User(user))
        }
    }

    fun onDismissDeleteDialogAction() {
        updateViewState { lastState ->
            lastState as UserListViewState.Success
            lastState.copy(userForDeletion = UserForDeletion.None)
        }
    }

    fun onDeleteUserAction() {
        updateViewState { lastState ->
            lastState as UserListViewState.Success
            lastState.copy(isLoading = true)
        }
        val userForDeletion = (viewState.value as UserListViewState.Success)
            .userForDeletion as UserForDeletion.User

        onDismissDeleteDialogAction()

        viewModelScope.launch {
            deleteUserUseCase(userForDeletion.user.id)
                .onSuccess {
                    fetchUsers()
                }
                .onFailure { error ->
                    updateViewState { lastState ->
                        lastState as UserListViewState.Success
                        lastState.copy(
                            errorMessage = error.message?.let(ErrorMessage::Text)
                                ?: ErrorMessage.Resource(R.string.error_delete_user),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onNavigateToAddUserHandled() {
        updateViewState { lastState ->
            lastState as UserListViewState.Success
            lastState.copy(shouldNavigateToAddUser = false)
        }
    }
}

sealed interface UserListViewState : ViewState {
    data object Initializing : UserListViewState
    data class Error(val errorMessage: ErrorMessage) : UserListViewState
    data class Success(
        val users: List<UserPresentationModel> = emptyList(),
        val shouldNavigateToAddUser: Boolean = false,
        val errorMessage: ErrorMessage? = null,
        val isLoading: Boolean = false,
        val userForDeletion: UserForDeletion = UserForDeletion.None
    ) : UserListViewState
}

sealed interface UserForDeletion {
    data object None : UserForDeletion
    data class User(val user: UserPresentationModel) : UserForDeletion
}
