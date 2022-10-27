package com.aaonri.app.data.home.model

data class InterestResponseItem(
    val active: Boolean,
    val iconName: String,
    val id: Int,
    val interestDesc: String,
    var isSelected: Boolean,
    var index: Int = -1
)