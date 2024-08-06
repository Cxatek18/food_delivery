package com.team.fooddelivery.domain.usecase.user

import com.team.fooddelivery.domain.entity.user.state.ResponseGetUserInfo
import com.team.fooddelivery.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserInfoUseCase(private val repository: UserRepository) {

    fun getUserInfo(userId: String): Flow<ResponseGetUserInfo> {
        return repository.getUserInfo(userId)
    }
}