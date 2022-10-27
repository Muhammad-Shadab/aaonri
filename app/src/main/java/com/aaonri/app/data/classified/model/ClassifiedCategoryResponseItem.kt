package com.aaonri.app.data.classified.model

data class ClassifiedCategoryResponseItem(
    val classifiedSubcategory: List<ClassifiedSubcategoryX>,
    val count: Int,
    val id: Int,
    val parentId: Any,
    val title: String
)