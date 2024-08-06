package com.team.fooddelivery.domain.entity.user

import android.os.Parcelable
import com.team.fooddelivery.domain.entity.product.Product
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id: Int,
    val products: List<Product>
) : Parcelable {
    constructor() : this(
        -1,
        listOf()
    )
}
