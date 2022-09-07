package com.aaonri.app.data.immigration.repository

import com.aaonri.app.data.immigration.api.ImmigrationApi
import com.aaonri.app.data.immigration.model.*
import javax.inject.Inject

class ImmigrationRepository @Inject constructor(private val immigrationApi: ImmigrationApi) {

    suspend fun getDiscussionCategory() = immigrationApi.getDiscussionCategory()

    suspend fun getDiscussionDetailsById(discussionId: String) =
        immigrationApi.getDiscussionDetailsById(discussionId)

    suspend fun getAllImmigrationDiscussion(getAllImmigrationRequest: GetAllImmigrationRequest) =
        immigrationApi.getAllImmigrationDiscussion(getAllImmigrationRequest)

    suspend fun replyDiscussion(replyDiscussionRequest: ReplyDiscussionRequest) =
        immigrationApi.replyDiscussion(replyDiscussionRequest)

    suspend fun postDiscussion(postDiscussionRequest: PostDiscussionRequest) =
        immigrationApi.postDiscussion(postDiscussionRequest)

    suspend fun deleteDiscussion(deleteDiscussionRequest: DeleteDiscussionRequest) =
        immigrationApi.deleteDiscussion(deleteDiscussionRequest)

    suspend fun updateDiscussion(updateDiscussionRequest: UpdateDiscussionRequest) =
        immigrationApi.updateDiscussion(updateDiscussionRequest)

}