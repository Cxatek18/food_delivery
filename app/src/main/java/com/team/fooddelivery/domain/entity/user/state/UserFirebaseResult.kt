package com.team.fooddelivery.domain.entity.user.state

sealed class UserFirebaseResult {

    data object Initial : UserFirebaseResult()

    data object UserIsRegister : UserFirebaseResult()

    data object UserErrorIsRegister : UserFirebaseResult()
}