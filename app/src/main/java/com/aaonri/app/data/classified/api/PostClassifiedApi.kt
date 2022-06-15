package com.aaonri.app.data.classified.api

import com.aaonri.app.data.classified.model.ClassifiedCategoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface PostClassifiedApi {

    @GET("/api/v1/category/getCategorySubCategoryMapping")
    suspend fun getClassifiedCategory(): Response<ClassifiedCategoryResponse>

}