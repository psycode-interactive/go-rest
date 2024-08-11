package com.psycodeinteractive.gorest.feature.users.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StatusApiModel {
    @SerialName("inactive")
    Inactive,
    @SerialName("active")
    Active
}
