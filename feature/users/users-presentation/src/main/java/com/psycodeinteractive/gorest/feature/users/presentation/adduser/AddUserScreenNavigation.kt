package com.psycodeinteractive.gorest.feature.users.presentation.adduser

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.psycodeinteractive.gorest.feature.users.presentation.UsersGraphRoute
import com.psycodeinteractive.gorest.feature.users.presentation.UsersGraphRoute.AddUser

const val wasUserAddedKey = "wasUserAdded"

fun NavController.navigateToAddUser() {
    navigate(AddUser)
}

fun NavGraphBuilder.addUserScreenRoute(
    navController: NavController,
    onNavigateUp: () -> Unit
) {
    dialog<AddUser> { backStackEntry ->
        AddUserScreen { wasUserAdded ->
            if (wasUserAdded) {
                navController.getBackStackEntry(UsersGraphRoute.UserList)
                    .savedStateHandle[wasUserAddedKey] = true
            }
            onNavigateUp()
        }
    }
}
