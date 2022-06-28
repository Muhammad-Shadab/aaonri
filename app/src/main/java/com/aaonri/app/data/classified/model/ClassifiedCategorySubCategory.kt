package com.aaonri.app.data.classified.model

data class ClassifiedCategorySubCategory(
    val classifiedSubcategory: List<ClassifiedSubcategory>,
    val count: Int,
    val id: Any,
    val parentId: Any,
    val title: String
)