package com.aaonri.app.data.advertise.model

data class AdvertiseDetailsResponse(
    val active: Boolean,
    val advertisementDetails: AdvertisementDetailsXX,
    val advertisementId: Int,
    val advertisementPageLocation: AdvertisementPageLocationX,
    val advertisementVasMap: List<AdvertiseVas>,
    val approved: Boolean,
    val createdOn: String,
    val emailId: String,
    val fromDate: String,
    val locationPlanRate: LocationPlanRateXX,
    val paymentStatus: String,
    val rate: Double,
    val rejectionReason: Any,
    val status: String,
    val template: TemplateX,
    val toDate: String
)