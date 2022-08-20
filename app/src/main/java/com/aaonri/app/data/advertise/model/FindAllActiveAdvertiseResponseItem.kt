package com.aaonri.app.data.advertise.model

import com.aaonri.app.data.main.adapter.CheckViewType

data class FindAllActiveAdvertiseResponseItem(
    val active: Boolean,
    val advertisementDetails: AdvertisementDetailsXXXXX,
    val advertisementId: Int,
    val advertisementPageLocation: AdvertisementPageLocationXX,
    val advertisementVasMap: List<AdvertisementVasMap>,
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
    val toDate: String,
    var checkViewType: CheckViewType
)