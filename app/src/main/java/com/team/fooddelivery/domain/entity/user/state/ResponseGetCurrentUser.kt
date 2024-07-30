package com.team.fooddelivery.domain.entity.user.state

import com.google.firebase.auth.FirebaseUser

sealed class ResponseGetCurrentUser {

    data object Initial : ResponseGetCurrentUser()

    data object Error : ResponseGetCurrentUser()

    data class UserSuccess(val user: FirebaseUser?) : ResponseGetCurrentUser()
}