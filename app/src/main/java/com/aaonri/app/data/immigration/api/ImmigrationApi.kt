package com.aaonri.app.data.immigration.api

import com.aaonri.app.data.immigration.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ImmigrationApi {

    @GET("/api/v1/discussion/getDiscCategories")
    suspend fun getDiscussionCategory(): Response<DiscussionCategoryResponse>

    @GET("/api/v1/discussion/getDiscussionReplies/{discussionId}")
    suspend fun getDiscussionDetailsById(
        @Path("discussionId") discussionId: String
    ): Response<DiscussionDetailsResponse>

    @POST("/api/v1/discussion/search")
    suspend fun getAllImmigrationDiscussion(
        @Body getAllImmigrationRequest: GetAllImmigrationRequest
    ): Response<GetAllDiscussionResponse>

    @POST("/api/v1/discussion/createNewDiscussionReply")
    suspend fun replyDiscussion(
        @Body replyDiscussionRequest: ReplyDiscussionRequest
    ): Response<ReplyDiscussionResponse>

    @POST("/api/v1/discussion/create")
    suspend fun postDiscussion(
        @Body postDiscussionRequest: PostDiscussionRequest
    ): Response<PostDiscussionResponse>
}
