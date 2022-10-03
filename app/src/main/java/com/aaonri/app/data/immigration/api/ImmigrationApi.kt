package com.aaonri.app.data.immigration.api

import com.aaonri.app.data.immigration.model.*
import retrofit2.Response
import retrofit2.http.*

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

    //@DELETE("/api/v1/discussion/delete")
    @HTTP(method = "DELETE", path = "/api/v1/discussion/delete", hasBody = true)
    suspend fun deleteDiscussion(
        @Body deleteDiscussionRequest: DeleteDiscussionRequest
    ): Response<DeleteDiscussionResponse>

    @PUT("/api/v1/discussion/update")
    suspend fun updateDiscussion(
        @Body updateDiscussionRequest: UpdateDiscussionRequest
    ): Response<UpdateDiscussionResponse>

    //@DELETE("/api/v1/discussion/deleteDiscReplies")
    @HTTP(method = "DELETE", path = "/api/v1/discussion/deleteDiscReplies", hasBody = true)
    suspend fun deleteImmigrationReply(
        @Body deleteReplyRequest: DeleteReplyRequest
    ): Response<DeleteReplyResponse>


}
