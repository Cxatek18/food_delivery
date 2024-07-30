package com.team.fooddelivery.domain.entity.product

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val photo: String,
    val price: Float,
    val sale: Float,
)
