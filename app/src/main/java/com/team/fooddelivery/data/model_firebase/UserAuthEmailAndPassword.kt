package com.team.fooddelivery.data.model_firebase

import com.team.fooddelivery.domain.entity.product.Product
import com.team.fooddelivery.domain.entity.user.Card
import com.team.fooddelivery.domain.entity.user.Order

data class UserAuthEmailAndPassword(
    val email: String,
    val username: String?,
    val photoUser: String?,
    val address: String?,
    val order: List<Order>?,
    val cart: List<Product>?,
    val cardData: Card?,
    val phoneUser: String,
    val isRegisterPhone: Boolean,
    val isAdmin: Boolean = false
)
