package com.team.fooddelivery.domain.usecase.user

import com.team.fooddelivery.domain.entity.user.state.UserFirebaseResult
import com.team.fooddelivery.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class VerifyCodeUseCase(private val repository: UserRepository) {

    fun verifyCode(
        verificationId: String,
        code: String,
        userPhone: String
    ): Flow<UserFirebaseResult> {
        return repository.verifyCode(verificationId, code, userPhone)
    }
}