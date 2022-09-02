package com.aaonri.app.data.immigration.model

data class Discussion(
    val approved: Boolean,
    val createdBy: String,
    val createdByPhone: String,
    val createdOn: String,
    val createdOnTS: String,
    val discCatId: Int,
    val discCatValue: String,
    val discussionDesc: String,
    val discussionId: Int,
    val discussionTopic: String,
    val latestReply: LatestReply,
    val noOfReplies: Int,
    val pageNumber: Int,
    val reasonForFeedBack: Any,
    val status: String,
    val updatedOnTS: String,
    val userId: String
)