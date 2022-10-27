package com.aaonri.app.data.immigration.model

data class PostDiscussionRequest(
    val discCatId: Int,
    val discussionDesc: String,
    val discussionTopic: String,
    val userId: String
)