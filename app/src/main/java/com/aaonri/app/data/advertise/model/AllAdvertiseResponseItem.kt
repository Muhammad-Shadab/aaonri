package com.aaonri.app.data.advertise.model

data class AllAdvertiseResponseItem(
    val advertisementId: Int,
    val createdOn: String,
    val days: Int,
    val fromDate: String,
    val rate: Double,
    val title: String,
    val toDate: String
)