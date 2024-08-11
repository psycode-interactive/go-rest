package com.psycodeinteractive.gorest.feature.users.data.mappers

import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.CreateUserApiModel
import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.GenderApiModel
import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.StatusApiModel
import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.UserApiModel
import com.psycodeinteractive.gorest.feature.users.domain.model.CreateUserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.GenderDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.StatusDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.UserDomainModel

fun UserApiModel.toDomain() = UserDomainModel(
    id = id,
    name = name,
    email = email,
    gender = gender.toDomain(),
    status = status.toDomain()
)

fun CreateUserDomainModel.toApi() = CreateUserApiModel(
    name = name,
    email = email,
    gender = gender.toApi(),
    status = status.toApi()
)

fun StatusApiModel.toDomain() = when (this) {
    StatusApiModel.Inactive -> StatusDomainModel.Inactive
    StatusApiModel.Active -> StatusDomainModel.Active
}

fun GenderApiModel.toDomain() = when (this) {
    GenderApiModel.Male -> GenderDomainModel.Male
    GenderApiModel.Female -> GenderDomainModel.Female
}

fun GenderDomainModel.toApi() = when (this) {
    GenderDomainModel.Male -> GenderApiModel.Male
    GenderDomainModel.Female -> GenderApiModel.Female
}

fun StatusDomainModel.toApi() = when (this) {
    StatusDomainModel.Inactive -> StatusApiModel.Inactive
    StatusDomainModel.Active -> StatusApiModel.Active
}

