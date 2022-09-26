package com.aaonri.app.data.event

import com.aaonri.app.data.event.model.EventCategoryResponseItem
import com.aaonri.app.data.event.model.EventDetailsResponse

object EventStaticData {

    private var eventCategories = ArrayList<EventCategoryResponseItem>()
    private var eventDetails: EventDetailsResponse? = null

    fun updateEventCategory(value: ArrayList<EventCategoryResponseItem>) {
        eventCategories = value
    }

    fun getEventCategory() = eventCategories

    fun updateEventDetails(value: EventDetailsResponse) {
        eventDetails = value
    }

    fun getEventDetailsData() = eventDetails
}