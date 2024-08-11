package com.psycodeinteractive.gorest.feature.users.domain.usecase

import com.psycodeinteractive.gorest.feature.users.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return userRepository.deleteUser(id)
    }
}
