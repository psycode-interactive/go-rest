package com.psycodeinteractive.gorest.feature.users.presentation.userlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.psycodeinteractive.gorest.core.presentation.OnLifecycle
import com.psycodeinteractive.gorest.feature.users.presentation.UsersGraphRoute.UserList
import com.psycodeinteractive.gorest.feature.users.presentation.adduser.wasUserAddedKey

fun NavGraphBuilder.userListScreenRoute(
    onNavigateToAddUser: () -> Unit
) {
    composable<UserList> { backStackEntry ->
        var wasUserAdded by remember { mutableStateOf(false) }
        OnLifecycle(
            minActiveState = Lifecycle.State.RESUMED
        ) {
            wasUserAdded = backStackEntry.savedStateHandle.remove<Boolean>(wasUserAddedKey) ?: false
        }

        UserListScreen(onNavigateToAddUser, wasUserAdded)
    }
}
