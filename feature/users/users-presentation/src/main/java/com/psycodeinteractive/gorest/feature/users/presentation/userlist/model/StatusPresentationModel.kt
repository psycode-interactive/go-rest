package com.psycodeinteractive.gorest.feature.users.presentation.userlist.model

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import com.psycodeinteractive.gorest.feature.users.presentation.R

@Serializable
enum class StatusPresentationModel(
    @StringRes val textResource: Int
) {
    Active(R.string.active),
    Inactive(R.string.inactive)
}
