package com.aaonri.app.data.classified.model

data class FavoriteClassifiedResponse(
    val classifiedCategorySubCategoryList: List<ClassifiedCategorySubCategoryX>,
    val classifieds: List<Classified>,
    val events: List<Any>,
    val jobs: List<Any>
)