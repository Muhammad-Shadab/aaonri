package com.aaonri.app.data.classified

import com.aaonri.app.data.classified.model.ClassifiedAdDetailsResponse
import com.aaonri.app.data.classified.model.ClassifiedCategoryResponseItem

object ClassifiedStaticData {

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