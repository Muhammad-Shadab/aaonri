package com.aaonri.app.data.classified.model

data class GetClassifiedByUserRequest(
    val category: String,
    val email: String,
    val fetchCatSubCat: Boolean,
    val keywords: String?,
    val location: String,
    val maxPrice: Double?,
    val minPrice: Double?,
    val myAdsOnly: Boolean,
    val popularOnAoonri: Any?,
    val subCategory: String,
    val zipCode: String?
)