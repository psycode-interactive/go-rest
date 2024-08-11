package com.psycodeinteractive.gorest.feature.users.presentation.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class ErrorMessage {
    data class Text(val message: String) : ErrorMessage()
    data class Resource(val messageResource: Int) : ErrorMessage()

    @Composable
    fun getMessage() = when(this) {
        is Text -> message
        is Resource -> stringResource(messageResource)
    }
}
