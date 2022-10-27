package com.aaonri.app.data.advertise.model

data class AdvertiseActiveVasResponseItem(
    val active: Boolean,
    val code: String,
    val createdOn: String,
    val description: String,
    val name: String,
    val rate: Double,
    val vasId: Int
)