package com.aaonri.app.data.advertise.model

data class AllAdvertiseResponseItem(
    val active: Boolean,
    val advertisementDetails: AdvertisementDetailsXXXXXX,
    val advertisementId: Int,
    val advertisementPageLocation: AdvertisementPageLocationXXX,
    val advertisementVasMap: List<AdvertisementVasMapX>,
    val approved: Boolean,
    val createdOn: String,
    val emailId: String,
    val fromDate: String,
    val locationPlanRate: LocationPlanRateXXXXX,
    val paymentStatus: String,
    val rate: Double,
    val rejectionReason: Any,
    val status: String,
    val template: TemplateXXX,
    val toDate: String
)