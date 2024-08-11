package com.psycodeinteractive.gorest

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                GoRestTheme {
                    GoRestAppEntryPoint(
                        navController = rememberNavController(),
                    )
                    AskForNotificationPermission()
                }
        }
    }

    @Composable
    private fun AskForNotificationPermission() {
        if (SDK_INT >= TIRAMISU) {
            val postNotificationPermissionState =
                rememberPermissionState(permission = POST_NOTIFICATIONS)
            LaunchedEffect(postNotificationPermissionState) {
                postNotificationPermissionState.launchPermissionRequest()
            }
        }
    }
}
