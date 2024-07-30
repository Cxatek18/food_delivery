package com.team.fooddelivery.domain.entity.user.state

import com.google.firebase.auth.FirebaseUser

sealed class ResponseUserAuthEmailAndPassword {

    data object Initial : ResponseUserAuthEmailAndPassword()

    data object Error : ResponseUserAuthEmailAndPassword()

    data class Success(val user: FirebaseUser?) : ResponseUserAuthEmailAndPassword()
}