package com.team.fooddelivery.data.mapper

import com.team.fooddelivery.data.model_firebase.UserAuthEmailAndPassword
import com.team.fooddelivery.domain.entity.user.Card
import com.team.fooddelivery.domain.entity.user.User

fun UserAuthEmailAndPassword.mapToUser(): User {
    return User(
        email = this.email,
        username = this.username ?: "",
        photoUser = this.photoUser ?: "",
        address = this.address ?: "",
        order = this.order ?: listOf(),
        cart = this.cart ?: listOf(),
        cardData = Card(),
        phoneUser = this.phoneUser,
        isRegisterPhone = false,
        isAdmin = false
    )
}
