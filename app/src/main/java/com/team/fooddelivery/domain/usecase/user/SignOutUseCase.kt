package com.team.fooddelivery.domain.usecase.user

import com.team.fooddelivery.domain.entity.user.state.ResponseUserSignOut
import com.team.fooddelivery.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class SignOutUseCase(private val repository: UserRepository) {

    fun signOut(): Flow<ResponseUserSignOut> {
        return repository.signOut()
    }
}