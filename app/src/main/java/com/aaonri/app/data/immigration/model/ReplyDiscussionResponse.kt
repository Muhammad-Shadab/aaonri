package com.aaonri.app.data.immigration.model

data class ReplyDiscussionResponse(
    val createdByUserId: Int,
    val discussionId: Int,
    val id: Int,
    val parentId: Int,
    val replyDesc: String
)