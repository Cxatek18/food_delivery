package com.team.fooddelivery.domain.entity.user

import com.team.fooddelivery.domain.entity.product.Product

data class Order(
    val id: Int,
    val products: List<Product>
)
