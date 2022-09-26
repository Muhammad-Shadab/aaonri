package com.aaonri.app.data.advertise.model

data class LocationPlanRateX(
    val active: Boolean,
    val createdOn: String,
    val days: Int,
    val description: String,
    val duration: String,
    val planId: Int,
    val rate: Double,
    val rateGroupCode: String,
    val title: String
)