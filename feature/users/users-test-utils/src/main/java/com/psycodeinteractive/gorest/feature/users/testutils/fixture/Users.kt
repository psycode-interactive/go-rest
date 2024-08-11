package com.psycodeinteractive.gorest.feature.users.testutils.fixture

import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.GenderApiModel
import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.StatusApiModel
import com.psycodeinteractive.gorest.feature.users.data.source.remote.model.UserApiModel
import com.psycodeinteractive.gorest.feature.users.domain.model.GenderDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.StatusDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.model.UserDomainModel
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.GenderPresentationModel
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.StatusPresentationModel
import com.psycodeinteractive.gorest.feature.users.presentation.userlist.model.UserPresentationModel

val presentationUser1 = UserPresentationModel(id = 1, name = "John Doe", email = "john.doe@example.com", gender = GenderPresentationModel.Male, status = StatusPresentationModel.Active)
val presentationUser2 = UserPresentationModel(id = 2, name = "Ferdek Doe", email = "testing@example.com", gender = GenderPresentationModel.Female, status = StatusPresentationModel.Inactive)

val domainUser1 = UserDomainModel(id = 1, name = "John Doe", email = "john.doe@example.com", gender = GenderDomainModel.Male, status = StatusDomainModel.Active)
val domainUser2 = UserDomainModel(id = 2, name = "Ferdek Doe", email = "testing@example.com", gender = GenderDomainModel.Female, status = StatusDomainModel.Inactive)

val apiUser1 = UserApiModel(id = 1, name = "John Doe", email = "john.doe@example.com", gender = GenderApiModel.Male, status = StatusApiModel.Active)
val apiUser2 = UserApiModel(id = 2, name = "Ferdek Doe", email = "testing@example.com", gender = GenderApiModel.Female, status = StatusApiModel.Inactive)
