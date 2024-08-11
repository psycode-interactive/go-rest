package com.psycodeinteractive.gorest.feature.users.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GenderApiModel {
    @SerialName("male")
    Male,
    @SerialName("female")
    Female
}
