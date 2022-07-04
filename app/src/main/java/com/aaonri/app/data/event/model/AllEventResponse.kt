package com.aaonri.app.data.event.model

data class AllEventResponse(
    val eventCategoryList: List<EventCategory>,
    val eventList: List<Event>,
    val eventLocationList: List<EventLocation>
)