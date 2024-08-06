package com.team.fooddelivery.domain.entity.user

import com.team.fooddelivery.domain.entity.product.Product

data class User(
    val email: String,
    val username: String,
    val photoUser: String,
    val address: String,
    val order: List<Order>,
    val cart: List<Product>,
    val cardData: Card?,
    val phoneUser: String = "",
    val isRegisterPhone: Boolean = false,
    val isAdmin: Boolean = false
)
