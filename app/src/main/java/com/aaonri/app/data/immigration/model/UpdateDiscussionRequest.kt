package com.aaonri.app.data.immigration.model

data class UpdateDiscussionRequest(
    val discCatId: Int,
    val discussionDesc: String,
    val discussionId: Int,
    val discussionTopic: String,
    val userId: String
)