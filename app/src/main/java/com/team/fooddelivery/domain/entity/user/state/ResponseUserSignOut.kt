package com.team.fooddelivery.domain.entity.user.state

sealed class ResponseUserSignOut {

    data object Initial : ResponseUserSignOut()

    data object Success : ResponseUserSignOut()

    data object Error : ResponseUserSignOut()
}