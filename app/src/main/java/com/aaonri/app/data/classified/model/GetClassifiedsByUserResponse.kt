package com.aaonri.app.data.classified.model

data class GetClassifiedsByUserResponse(
    val classifiedCategorySubCategoryList: List<ClassifiedCategorySubCategory>,
    val userAdsList: List<UserAds>
)