package com.team.fooddelivery.data.model_firebase

import android.os.Parcelable
import com.team.fooddelivery.domain.entity.product.Product
import com.team.fooddelivery.domain.entity.user.Card
import com.team.fooddelivery.domain.entity.user.Order
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAuthEmailAndPassword(
    val email: String = "",
    val username: String? = null,
    val photoUser: String? = null,
    val address: String? = null,
    val order: List<Order>? = null,
    val cart: List<Product>? = null,
    val cardData: Card? = null,
    val phoneUser: String = "",
    val isRegisterPhone: Boolean = false,
    val isAdmin: Boolean = false
) : Parcelable {
    constructor() : this(
        "",
        "",
        "",
        "",
        listOf(Order()),
        listOf(Product()),
        Card(),
        "",
        false,
        false
    )
}
