package com.aaonri.app.data.advertise.model

data class AdvertisePageLocationResponseItem(
    val active: Boolean,
    val advertisementPage: AdvertisementPageXX,
    val createdOn: String,
    val description: String,
    val height: Int,
    val imageName: String,
    val locationCode: String,
    val locationId: Int,
    val locationName: String,
    val locationPlanRate: LocationPlanRate,
    val title: String,
    val type: String,
    val width: Int,
    var isSelected: Boolean
)