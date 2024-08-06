package com.team.fooddelivery.domain.repository.user

import com.team.fooddelivery.domain.entity.user.state.CodePhoneResult
import com.team.fooddelivery.domain.entity.user.state.ResponseGetCurrentUser
import com.team.fooddelivery.domain.entity.user.state.ResponseGetUserInfo
import com.team.fooddelivery.domain.entity.user.state.ResponseUserAuthEmailAndPassword
import com.team.fooddelivery.domain.entity.user.state.ResponseUserResetPassword
import com.team.fooddelivery.domain.entity.user.state.ResponseUserSignOut
import com.team.fooddelivery.domain.entity.user.state.UserFirebaseResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getCurrentUser(): Flow<ResponseGetCurrentUser?>

    fun authEmailAndPassword(
        email: String,
        password: String
    ): Flow<UserFirebaseResult>

    fun sendVerifyCode(
        userPhone: String
    ): Flow<CodePhoneResult>

    fun verifyCode(
        verificationId: String,
        code: String,
        userPhone: String
    ): Flow<UserFirebaseResult>

    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResponseUserAuthEmailAndPassword?>

    fun restPassword(
        email: String
    ): Flow<ResponseUserResetPassword>

    fun signOut(): Flow<ResponseUserSignOut>

    fun getUserInfo(userId: String): Flow<ResponseGetUserInfo>
}