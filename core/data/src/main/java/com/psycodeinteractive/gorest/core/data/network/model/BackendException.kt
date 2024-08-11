package com.psycodeinteractive.gorest.core.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BackendException(
    val field: String,
    val message: String
)
