package com.aaonri.app.data.immigration.model

data class ReplyDiscussionRequest(
    val createdByUserId: Int,
    val discussionId: String,
    val id: Int,
    val parentId: Int,
    val replyDesc: String
)