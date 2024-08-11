package com.psycodeinteractive.gorest.feature.users.domain.model

data class UserDomainModel(
    val id: Long,
    val name: String,
    val email: String,
    val gender: GenderDomainModel,
    val status: StatusDomainModel
)
