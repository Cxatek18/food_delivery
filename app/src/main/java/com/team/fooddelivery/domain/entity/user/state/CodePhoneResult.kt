package com.team.fooddelivery.domain.entity.user.state

sealed class CodePhoneResult {

    data object Initial : CodePhoneResult()

    data class Success(val result: String) : CodePhoneResult()

    data object Error : CodePhoneResult()
}