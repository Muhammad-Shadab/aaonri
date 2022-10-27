package com.aaonri.app.data.immigration.model

data class DeleteReplyResponse(
    val discRepliesId: Int,
    val message: String,
    val status: Boolean
)