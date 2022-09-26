package com.aaonri.app.data.advertise.model

data class UpdateAdvertiseRequest(
    val advertisementDetails: AdvertisementDetailsXXXX,
    val advertisementId: Int,
    val codes: List<Any>
)