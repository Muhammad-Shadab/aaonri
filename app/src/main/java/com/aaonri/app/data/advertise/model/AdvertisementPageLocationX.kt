package com.aaonri.app.data.advertise.model

data class AdvertisementPageLocationX(
    val active: Boolean,
    val advertisementPage: AdvertisementPageX,
    val createdOn: String,
    val description: String,
    val height: Int,
    val imageName: String,
    val locationCode: String,
    val locationId: Int,
    val locationName: String,
    val locationPlanRate: LocationPlanRateXX,
    val title: String,
    val type: String,
    val width: Int
)