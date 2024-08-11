package com.psycodeinteractive.gorest.feature.users.domain.model

data class CreateUserDomainModel(
    val name: String,
    val email: String,
    val gender: GenderDomainModel,
    val status: StatusDomainModel
)
