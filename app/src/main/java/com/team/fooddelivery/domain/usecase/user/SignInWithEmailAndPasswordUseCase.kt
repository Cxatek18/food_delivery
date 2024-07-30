package com.team.fooddelivery.domain.usecase.user

import com.team.fooddelivery.domain.entity.user.state.ResponseUserAuthEmailAndPassword
import com.team.fooddelivery.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class SignInWithEmailAndPasswordUseCase(private val repository: UserRepository) {

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResponseUserAuthEmailAndPassword?> {
        return repository.signInWithEmailAndPassword(email, password)
    }
}