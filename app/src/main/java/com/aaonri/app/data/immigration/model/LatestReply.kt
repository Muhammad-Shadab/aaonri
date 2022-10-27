package com.aaonri.app.data.immigration.model

data class LatestReply(
    val createdBy: Int,
    val createdByName: String,
    val createdDate: String,
    val discRepliesId: Int,
    val discussion: Any,
    val parentId: Int,
    val replyDesc: String
)