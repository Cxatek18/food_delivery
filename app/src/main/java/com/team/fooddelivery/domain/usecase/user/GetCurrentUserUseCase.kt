package com.team.fooddelivery.domain.usecase.user

import com.team.fooddelivery.domain.entity.user.state.ResponseGetCurrentUser
import com.team.fooddelivery.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(private val repository: UserRepository) {

    fun getCurrentUser(): Flow<ResponseGetCurrentUser?> {
        return repository.getCurrentUser()
    }
}