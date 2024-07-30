package com.team.fooddelivery.domain.usecase.user

import com.team.fooddelivery.domain.entity.user.state.UserFirebaseResult
import com.team.fooddelivery.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class AuthEmailAndPasswordUseCase(private val repository: UserRepository) {

    fun authEmailAndPassword(
        email: String,
        password: String
    ): Flow<UserFirebaseResult> {
        return repository.authEmailAndPassword(email, password)
    }
}