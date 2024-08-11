package com.psycodeinteractive.gorest

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.psycodeinteractive.gorest.feature.users.presentation.UsersGraphRoute
import com.psycodeinteractive.gorest.feature.users.presentation.usersGraph

@Composable
fun GoRestAppEntryPoint(
    navController: NavHostController,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) { padding ->
        NavHost(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
            navController = navController,
            startDestination = UsersGraphRoute.Root,
            contentAlignment = Alignment.TopStart
        ) {
            usersGraph(
                navController = navController,
            )
        }
    }
}
