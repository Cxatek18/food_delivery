package com.team.fooddelivery.domain.entity.user

import com.team.fooddelivery.domain.entity.product.Product

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val username: String,
    val photoUser: String,
    val address: String,
    val order: List<Order>,
    val cart: List<Product>,
    val cardData: Card?
)
