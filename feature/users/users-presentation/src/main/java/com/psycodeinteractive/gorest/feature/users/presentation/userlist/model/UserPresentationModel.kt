package com.psycodeinteractive.gorest.feature.users.presentation.userlist.model

import com.psycodeinteractive.gorest.feature.users.domain.model.GenderDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.StatusDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.UserDomainModel
import kotlinx.serialization.Serializable

@Serializable
data class UserPresentationModel(
    val id: Long,
    val name: String,
    val email: String,
    val gender: GenderPresentationModel,
    val status: StatusPresentationModel
)

fun UserDomainModel.toPresentation() = UserPresentationModel(
    id = id,
    name = name,
    email = email,
    gender = gender.toPresentation(),
    status = status.toPresentation()
)

fun GenderDomainModel.toPresentation() = when (this) {
    GenderDomainModel.Male -> GenderPresentationModel.Male
    GenderDomainModel.Female -> GenderPresentationModel.Female
}

fun StatusDomainModel.toPresentation() = when (this) {
    StatusDomainModel.Active -> StatusPresentationModel.Active
    StatusDomainModel.Inactive -> StatusPresentationModel.Inactive
}

fun GenderPresentationModel.toDomain() = when (this) {
    GenderPresentationModel.Female -> GenderDomainModel.Female
    GenderPresentationModel.Male -> GenderDomainModel.Male
}

fun StatusPresentationModel.toDomain() = when (this) {
    StatusPresentationModel.Active -> StatusDomainModel.Active
    StatusPresentationModel.Inactive -> StatusDomainModel.Inactive
}
