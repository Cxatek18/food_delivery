package com.team.fooddelivery.domain.entity.user.state

import com.team.fooddelivery.domain.entity.user.User

sealed class ResponseGetUserInfo {

    data object Initial : ResponseGetUserInfo()

    data class Success(val userObj: User) : ResponseGetUserInfo()

    data object Error : ResponseGetUserInfo()
}