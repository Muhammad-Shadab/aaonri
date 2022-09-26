package com.aaonri.app.data.advertise.model

data class PostAdvertiseResponse(
    val active: Boolean,
    val advertisementDetails: AdvertisementDetailsX,
    val advertisementId: Int,
    val advertisementPageLocation: AdvertisementPageLocation,
    val advertisementVasMap: List<Any>,
    val approved: Boolean,
    val createdOn: String,
    val emailId: String,
    val fromDate: String,
    val locationPlanRate: LocationPlanRateX,
    val paymentStatus: String,
    val rate: Double,
    val rejectionReason: Any,
    val status: String,
    val template: Template,
    val toDate: String
)