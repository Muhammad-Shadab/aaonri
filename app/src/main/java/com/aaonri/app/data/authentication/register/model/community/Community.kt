package com.aaonri.app.data.authentication.register.model.community

data class Community(
    val communityId: Int,
    val communityName: String,
    val description: String,
    var isSelected: Boolean
)