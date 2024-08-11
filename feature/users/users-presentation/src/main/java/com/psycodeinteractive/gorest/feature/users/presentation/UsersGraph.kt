package com.psycodeinteractive.gorest.feature.users.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.psycodeinteractive.gorest.feature.users.presentation.adduser.addUserScreenRoute
import com.psycodeinteractive.gorest.feature.users.presentation.adduser.navigateToAddUser
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.userListScreenRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface UsersGraphRoute {
    @Serializable
    data object Root : UsersGraphRoute
    @Serializable
    data object UserList : UsersGraphRoute
    @Serializable
    data object AddUser : UsersGraphRoute
}

fun NavGraphBuilder.usersGraph(
    navController: NavHostController
) {
    navigation<UsersGraphRoute.Root>(
        startDestination = UsersGraphRoute.UserList,
    ) {
        userListScreenRoute(
            onNavigateToAddUser = navController::navigateToAddUser
        )
        addUserScreenRoute(
            navController = navController,
            onNavigateUp = navController::popBackStack
        )
    }
}
