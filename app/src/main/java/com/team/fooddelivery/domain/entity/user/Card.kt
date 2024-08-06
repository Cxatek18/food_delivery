package com.team.fooddelivery.domain.entity.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Card(
    val numberCard: String,
    val fullNameOwner: String,
    val dateEndings: String,
    val cvv: String
) : Parcelable {
    constructor() : this(
        "",
        "",
        "",
        ""
    )
}
