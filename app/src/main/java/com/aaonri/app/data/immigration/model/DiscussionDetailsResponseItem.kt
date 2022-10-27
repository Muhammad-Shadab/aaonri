package com.aaonri.app.data.immigration.model

data class DiscussionDetailsResponseItem(
    val createdBy: Int,
    val createdDate: Any,
    val createdOn: String,
    val createdOnTS: String,
    val discRepliesId: Int,
    val discussionDTO: Any,
    val discussionId: Int,
    val parentId: Int,
    val replyDesc: String,
    val updatedOnTS: Any,
    val userEmail: String,
    val userFullName: String,
    val userImage: String?,
    val userName: Any?
)