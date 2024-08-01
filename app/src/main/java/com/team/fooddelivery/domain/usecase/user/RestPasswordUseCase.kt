package com.team.fooddelivery.domain.usecase.user

import com.team.fooddelivery.domain.entity.user.state.ResponseUserResetPassword
import com.team.fooddelivery.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class RestPasswordUseCase(private val repository: UserRepository) {

    fun restPassword(email: String): Flow<ResponseUserResetPassword> {
        return repository.restPassword(email)
    }
}