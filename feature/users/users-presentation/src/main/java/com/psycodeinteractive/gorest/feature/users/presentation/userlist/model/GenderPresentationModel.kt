package com.psycodeinteractive.gorest.feature.users.presentation.userlist.model

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import com.psycodeinteractive.gorest.feature.users.presentation.R

@Serializable
enum class GenderPresentationModel(
    @StringRes val textResource: Int
) {
    Male(R.string.male),
    Female(R.string.female)
}
