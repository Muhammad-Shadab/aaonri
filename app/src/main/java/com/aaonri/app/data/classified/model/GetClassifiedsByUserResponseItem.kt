package com.aaonri.app.data.classified.model

data class GetClassifiedsByUserResponseItem(
    val classifiedSubcategory: List<ClassifiedSubcategory>,
    val count: Int,
    val id: Int,
    val parentId: Any,
    val title: String
)