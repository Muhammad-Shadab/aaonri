package com.aaonri.app.utils

import com.aaonri.app.data.classified.model.ClassifiedAdDetailsResponse
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponseItem

object ClassifiedCategoriesList {

    private var categoriesList = ArrayList<ClassifiedCategoryResponseItem>()

    private var classifiedAddDetails: ClassifiedAdDetailsResponse? = null

    fun updateCategoryList(value: ArrayList<ClassifiedCategoryResponseItem>) {
        categoriesList = value
    }

    fun getCategoryList() = categoriesList

    fun updateAddDetails(value: ClassifiedAdDetailsResponse) {
        classifiedAddDetails = value
    }

    fun getAddDetails() = classifiedAddDetails

}