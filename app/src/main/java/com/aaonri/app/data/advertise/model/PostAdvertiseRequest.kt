package com.aaonri.app.data.advertise.model

data class PostAdvertiseRequest(
    val active: Boolean,
    val advertisementDetails: AdvertisementDetails,
    val emailId: String,
    val locationId: Int,
    val paymentStatus: String,
    val planId: Int,
    val rate: Int,
    val templateCode: String,
    val vasCodes: List<Any>
)