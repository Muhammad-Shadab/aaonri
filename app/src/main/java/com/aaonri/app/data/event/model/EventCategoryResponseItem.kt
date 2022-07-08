package com.aaonri.app.data.event.model

data class EventCategoryResponseItem(
    val active: Boolean,
    val id: Int,
    val parentId: Int,
    val title: String
)