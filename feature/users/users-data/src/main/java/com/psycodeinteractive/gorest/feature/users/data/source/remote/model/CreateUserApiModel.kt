package com.psycodeinteractive.gorest.feature.users.data.source.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserApiModel(
    val name: String,
    val email: String,
    val gender: GenderApiModel,
    val status: StatusApiModel
)
