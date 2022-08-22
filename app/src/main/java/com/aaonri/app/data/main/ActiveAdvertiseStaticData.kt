package com.aaonri.app.data.main

import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponse
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.main.adapter.CheckViewType

object ActiveAdvertiseStaticData {

    private var activeAdvertiseResponse: FindAllActiveAdvertiseResponse? = null

    /** classified landing page data **/
    private var classifiedJustAboveFooterTextOnly =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var classifiedJustAboveFooterImageOnly =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var classifiedJustAboveBottomTabBOTH =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var classifiedTopBanner =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    /** classified details screen advertise **/
    private var advertiseOnClassifiedDetails =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    /** event landing page data **/
    private var eventJustAboveFooterTextOnly =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var eventJustAboveFooterImageOnly =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var eventJustAboveBottomTabBOTH =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var eventTopBanner =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    /** event details screen advertise **/
    private var advertiseOnEventDetails =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    fun updateActiveAdvertiseDetails(value: FindAllActiveAdvertiseResponse?) {
        activeAdvertiseResponse = value
        addClassifiedData()
    }

    fun addClassifiedData() {
        activeAdvertiseResponse?.forEachIndexed { index, data ->
            /** classified landing page data **/
            if (data.advertisementPageLocation.locationId == 19) {
                if (!classifiedJustAboveFooterTextOnly.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.TEXT_ONLY
                    classifiedJustAboveFooterTextOnly.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 18) {
                if (!classifiedJustAboveFooterImageOnly.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.IMAGE_ONLY
                    classifiedJustAboveFooterImageOnly.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 17) {
                if (!classifiedJustAboveBottomTabBOTH.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.BOTH
                    classifiedJustAboveBottomTabBOTH.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 16) {
                if (!classifiedTopBanner.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.BOTH
                    classifiedTopBanner.add(activeAdvertiseResponse!![index])
                }
            }

            /** classified details screen advertise **/
            if (data.advertisementPageLocation.locationId == 32 || data.advertisementPageLocation.locationId == 33) {
                if (!advertiseOnClassifiedDetails.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.BOTH
                    advertiseOnClassifiedDetails.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 34) {
                if (!advertiseOnClassifiedDetails.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.IMAGE_ONLY
                    advertiseOnClassifiedDetails.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 35) {
                if (!advertiseOnClassifiedDetails.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.TEXT_ONLY
                    advertiseOnClassifiedDetails.add(activeAdvertiseResponse!![index])
                }
            }

            /** event landing page data **/
            if (data.advertisementPageLocation.locationId == 23) {
                if (!eventJustAboveFooterTextOnly.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.TEXT_ONLY
                    eventJustAboveFooterTextOnly.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 22) {
                if (!eventJustAboveFooterImageOnly.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.IMAGE_ONLY
                    eventJustAboveFooterImageOnly.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 21) {
                if (!eventJustAboveBottomTabBOTH.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.BOTH
                    eventJustAboveBottomTabBOTH.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 20) {
                if (!eventTopBanner.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.BOTH
                    eventTopBanner.add(activeAdvertiseResponse!![index])
                }
            }

            /** event details screen advertise **/
            if (data.advertisementPageLocation.locationId == 36) {
                if (!advertiseOnEventDetails.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.BOTH
                    advertiseOnEventDetails.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 37) {
                if (!advertiseOnEventDetails.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.IMAGE_ONLY
                    advertiseOnEventDetails.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 38) {
                if (!advertiseOnEventDetails.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.TEXT_ONLY
                    advertiseOnEventDetails.add(activeAdvertiseResponse!![index])
                }
            }
        }
    }

    fun getActiveAdvertiseDetails() = activeAdvertiseResponse

    /** classified data advertise **/
    fun getClassifiedJustAboveFooterTextOnly() = classifiedJustAboveFooterTextOnly
    fun getClassifiedJustAboveFooterImageOnly() = classifiedJustAboveFooterImageOnly
    fun getClassifiedJustAboveBottomTabBOTH() = classifiedJustAboveBottomTabBOTH
    fun getAdvertiseOnClassifiedDetails() = advertiseOnClassifiedDetails
    fun getClassifiedTopBanner() = classifiedTopBanner

    /** event data advertise **/
    fun getEventJustAboveFooterTextOnly() = eventJustAboveFooterTextOnly
    fun getEventJustAboveFooterImageOnly() = eventJustAboveFooterImageOnly
    fun getEventJustAboveBottomTabBOTH() = eventJustAboveBottomTabBOTH
    fun getAdvertiseOnEventDetails() = advertiseOnEventDetails
    fun getEventTopBanner() = eventTopBanner

}