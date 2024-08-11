package com.psycodeinteractive.gorest.feature.users.presentation.userlist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.psycodeinteractive.gorest.core.presentation.PreviewWithThemes
import com.psycodeinteractive.gorest.core.presentation.Screen
import com.psycodeinteractive.gorest.feature.users.presentation.R
import com.psycodeinteractive.gorest.feature.users.presentation.model.ErrorMessage
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.GenderPresentationModel
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.StatusPresentationModel
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.UserPresentationModel

@Composable
fun UserListScreen(
    onNavigateToAddUser: () -> Unit,
    wasUserAdded: Boolean
) {
    Screen<UserListViewModel, _> {
        UserListScreenContent(
            viewState = viewState,
            onTryAgainClick = viewModel::onTryAgainAction,
            onAddUserClick = viewModel::onAddUserAction,
            onNavigateToAddUser = onNavigateToAddUser,
            onNavigateToAddUserHandled = viewModel::onNavigateToAddUserHandled,
            onDismissDeleteUserDialog = viewModel::onDismissDeleteDialogAction,
            onDeleteUserAction = viewModel::onDeleteUserAction,
            onUserLongPress = viewModel::onUserLongPressAction
        )

        if (wasUserAdded) {
            val context = LocalContext.current
            val message = stringResource(R.string.add_user_success)
            LaunchedEffect(Unit) {
                viewModel.onUserWasAdded()
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
private fun UserListScreenContent(
    viewState: UserListViewState,
    onTryAgainClick: () -> Unit,
    onAddUserClick: () -> Unit,
    onNavigateToAddUser: () -> Unit,
    onNavigateToAddUserHandled: () -> Unit,
    onDismissDeleteUserDialog: () -> Unit,
    onDeleteUserAction: () -> Unit,
    onUserLongPress: (user: UserPresentationModel) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TopBar(onAddUserClick = onAddUserClick)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = viewState) {
                UserListViewState.Initializing -> CircularProgressIndicator()
                is UserListViewState.Error -> UserListError(
                    errorMessage = state.errorMessage,
                    onTryAgainClick = onTryAgainClick
                )

                is UserListViewState.Success -> {
                    UserListSuccess(
                        users = state.users,
                        onUserLongPress = onUserLongPress,
                        isLoading = state.isLoading
                    )

                    if (state.userForDeletion is UserForDeletion.User) {
                        DeleteUserDialog(
                            onDismissRequest = onDismissDeleteUserDialog,
                            onConfirmAction = onDeleteUserAction
                        )
                    }

                    if (state.shouldNavigateToAddUser) {
                        LaunchedEffect(Unit) {
                            onNavigateToAddUser()
                            onNavigateToAddUserHandled()
                        }
                    }

                    state.errorMessage?.run {
                        val context = LocalContext.current
                        val message = getMessage()
                        LaunchedEffect(Unit) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.UserListSuccess(
    users: List<UserPresentationModel>,
    onUserLongPress: (user: UserPresentationModel) -> Unit,
    isLoading: Boolean
) {
    if (users.isEmpty()) {
        UserListEmpty()
    } else {
        val spacing = 12.dp
        val lazyListState = rememberLazyListState()
        if (isLoading.not()) {
            LaunchedEffect(Unit) {
                lazyListState.animateScrollToItem(0)
            }
        }
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            itemsIndexed(
                items = users,
                key = { _, user -> user.id }
            ) { index, user ->
                Column(
                    modifier = Modifier.animateItem()
                ) {
                    UserListItem(user, onUserLongPress)
                    if (index != users.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(top = spacing))
                    }
                }
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun UserListItem(
    user: UserPresentationModel,
    onUserLongPress: (user: UserPresentationModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .combinedClickable(
                onClick = { /* no-op */ },
                onLongClick = { onUserLongPress(user) }
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = user.name,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
        )
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = "${stringResource(R.string.gender)}: ${stringResource(user.gender.textResource)}",
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = "${stringResource(R.string.status)}: ${stringResource(user.status.textResource)}",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun UserListError(
    errorMessage: ErrorMessage,
    onTryAgainClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage.getMessage(),
            style = MaterialTheme.typography.labelLarge,
        )
        TextButton(
            onClick = onTryAgainClick,
        ) {
            Text(
                text = stringResource(R.string.try_again),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun UserListEmpty() {
    Text(
        text = stringResource(R.string.empty_user_list),
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
private fun TopBar(
    onAddUserClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.go_rest_users),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        actions = {
            IconButton(
                onClick = onAddUserClick
            ) {
                Icon(
                    imageVector = Icons.Sharp.Add,
                    contentDescription = stringResource(R.string.add_user)
                )
            }
        }
    )
}

@Composable
private fun DeleteUserDialog(
    onDismissRequest: () -> Unit,
    onConfirmAction: () -> Unit,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        title = {
            Text(
                text = stringResource(R.string.delete_user_title),
                style = MaterialTheme.typography.labelLarge,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.delete_user_confirmation),
                style = MaterialTheme.typography.labelLarge,
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmAction
            ) {
                Text(
                    text = stringResource(R.string.yes),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = stringResource(R.string.no),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    )
}

@PreviewWithThemes
@Composable
private fun UserListSuccessPreview() {
    UserListScreenContent(
        viewState = UserListViewState.Success(
            users = listOf(
                UserPresentationModel(
                    id = 1,
                    name = "John Doe",
                    email = "testing@test.com,",
                    gender = GenderPresentationModel.Male,
                    status = StatusPresentationModel.Active
                ),
                UserPresentationModel(
                    id = 2,
                    name = "Jane Doe",
                    email = "testingMs@test.com,",
                    gender = GenderPresentationModel.Male,
                    status = StatusPresentationModel.Active
                )
            )
        ),
        onTryAgainClick = {},
        onAddUserClick = {},
        onNavigateToAddUser = {},
        onNavigateToAddUserHandled = {},
        onDismissDeleteUserDialog = {},
        onDeleteUserAction = {},
        onUserLongPress = {}
    )
}

@PreviewWithThemes
@Composable
private fun UserListErrorPreview() {
    UserListScreenContent(
        viewState = UserListViewState.Error(
            ErrorMessage.Text("Every failure is a step to success")
        ),
        onTryAgainClick = {},
        onAddUserClick = {},
        onNavigateToAddUser = {},
        onNavigateToAddUserHandled = {},
        onDismissDeleteUserDialog = {},
        onDeleteUserAction = {},
        onUserLongPress = {}
    )
}

@PreviewWithThemes
@Composable
private fun UserListEmptyPreview() {
    UserListScreenContent(
        viewState = UserListViewState.Success(users = emptyList()),
        onTryAgainClick = {},
        onAddUserClick = {},
        onNavigateToAddUser = {},
        onNavigateToAddUserHandled = {},
        onDismissDeleteUserDialog = {},
        onDeleteUserAction = {},
        onUserLongPress = {}
    )
}

@PreviewWithThemes
@Composable
private fun UserListLoadingPreview() {
    UserListScreenContent(
        viewState = UserListViewState.Initializing,
        onTryAgainClick = {},
        onAddUserClick = {},
        onNavigateToAddUser = {},
        onNavigateToAddUserHandled = {},
        onDismissDeleteUserDialog = {},
        onDeleteUserAction = {},
        onUserLongPress = {}
    )
}
