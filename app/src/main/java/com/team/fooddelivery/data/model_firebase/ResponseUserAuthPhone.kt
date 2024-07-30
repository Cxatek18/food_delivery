package com.team.fooddelivery.data.model_firebase

sealed class ResponseUserAuthPhone {

    data object Initial : ResponseUserAuthPhone()

    data class Success(val verifyCode: String) : ResponseUserAuthPhone()

    data object Error : ResponseUserAuthPhone()
}