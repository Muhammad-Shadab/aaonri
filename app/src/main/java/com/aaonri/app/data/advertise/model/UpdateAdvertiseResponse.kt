package com.aaonri.app.data.advertise.model

data class UpdateAdvertiseResponse(
    val active: Boolean,
    val advertisementDetails: AdvertisementDetailsXXX,
    val advertisementId: Int,
    val advertisementPageLocation: AdvertisementPageLocationXX,
    val advertisementVasMap: List<Any>,
    val approved: Boolean,
    val createdOn: String,
    val emailId: String,
    val fromDate: String,
    val locationPlanRate: LocationPlanRateXXXX,
    val paymentStatus: String,
    val rate: Double,
    val rejectionReason: Any,
    val status: String,
    val template: TemplateXX,
    val toDate: String
)