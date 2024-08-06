package com.team.fooddelivery.domain.entity.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val photo: String,
    val price: Float,
    val sale: Float,
) : Parcelable {
    constructor() : this(
        -1,
        "",
        "",
        "",
        0.0f,
        0.0f
    )
}
