package com.team.fooddelivery.domain.entity.user.state

sealed class ResponseUserResetPassword {

    data object Initial : ResponseUserResetPassword()

    data object Success : ResponseUserResetPassword()

    data object Error : ResponseUserResetPassword()
}