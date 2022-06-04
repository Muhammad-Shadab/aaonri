package com.aaonri.app.data.authentication.register.model.services

data class ServicesResponseItem(
    val active: Boolean,
    val iconName: String,
    var id: Int,
    val interestDesc: String
)