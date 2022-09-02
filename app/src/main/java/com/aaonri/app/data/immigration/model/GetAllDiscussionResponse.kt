package com.aaonri.app.data.immigration.model

data class GetAllDiscussionResponse(
    val discCatList: List<DiscCat>,
    val discussionList: List<Discussion>
)