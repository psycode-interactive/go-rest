package com.psycodeinteractive.gorest.feature.users.data.source.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UserApiModel(
    val id: Long,
    val name: String,
    val email: String,
    val gender: GenderApiModel,
    val status: StatusApiModel
)
