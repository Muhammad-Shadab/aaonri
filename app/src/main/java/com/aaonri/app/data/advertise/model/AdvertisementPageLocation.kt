package com.aaonri.app.data.advertise.model

data class AdvertisementPageLocation(
    val active: Boolean,
    val advertisementPage: AdvertisementPage,
    val createdOn: String,
    val description: String,
    val height: Int,
    val imageName: String,
    val locationCode: String,
    val locationId: Int,
    val locationName: String,
    val locationPlanRate: LocationPlanRateX,
    val title: String,
    val type: String,
    val width: Int
)