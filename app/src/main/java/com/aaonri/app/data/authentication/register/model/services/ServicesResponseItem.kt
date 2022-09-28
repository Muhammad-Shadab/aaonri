package com.aaonri.app.data.authentication.register.model.services

data class ServicesResponseItem(
    var active: Boolean,
    val iconName: String,
    var id: Int,
    val interestDesc: String,
    var isSelected: Boolean
)