package com.aaonri.app.data.immigration.api

import com.aaonri.app.data.immigration.model.DiscussionCategoryResponse
import com.aaonri.app.data.immigration.model.GetAllDiscussionResponse
import com.aaonri.app.data.immigration.model.GetAllImmigrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ImmigrationApi {

    @GET("/api/v1/discussion/getDiscCategories")
    suspend fun getDiscussionCategory(): Response<DiscussionCategoryResponse>

    @POST("/api/v1/discussion/search")
    suspend fun getAllImmigrationDiscussion(
        @Body getAllImmigrationRequest: GetAllImmigrationRequest
    ): Response<GetAllDiscussionResponse>

}