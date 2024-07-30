package com.team.fooddelivery.domain.usecase.user

import com.team.fooddelivery.domain.entity.user.state.CodePhoneResult
import com.team.fooddelivery.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class SendVerifyCodeUseCase(private val repository: UserRepository) {

    fun sendVerifyCode(userPhone: String): Flow<CodePhoneResult> {
        return repository.sendVerifyCode(userPhone)
    }
}