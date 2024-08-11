package com.psycodeinteractive.gorest.feature.users.domain.usecase

import com.psycodeinteractive.gorest.feature.users.domain.model.CreateUserDomainModel
import com.psycodeinteractive.gorest.feature.users.domain.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: CreateUserDomainModel): Result<Unit> {
        return userRepository.addUser(user)
    }
}
