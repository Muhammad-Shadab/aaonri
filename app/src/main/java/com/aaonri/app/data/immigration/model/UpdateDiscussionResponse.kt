package com.aaonri.app.data.immigration.model

data class UpdateDiscussionResponse(
    val approved: Boolean,
    val createdBy: Any,
    val createdOn: Any,
    val createdOnTS: Any,
    val discCatId: Int,
    val discCatValue: Any,
    val discussionDesc: String,
    val discussionId: Int,
    val discussionTopic: String,
    val noOfReplies: Int,
    val pageNumber: Int,
    val reasonForFeedBack: Any,
    val status: Any,
    val updatedOnTS: Any,
    val userId: String
)