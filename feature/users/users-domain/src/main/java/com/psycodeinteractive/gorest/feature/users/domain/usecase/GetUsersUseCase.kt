package com.psycodeinteractive.gorest.feature.users.domain.usecase

import com.psycodeinteractive.gorest.feature.users.domain.model.UserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<UserDomainModel>> {
        return userRepository.getUsers()
    }
}
