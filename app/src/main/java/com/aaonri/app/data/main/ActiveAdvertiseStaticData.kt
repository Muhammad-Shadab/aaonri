package com.aaonri.app.data.main

import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponse
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem

object ActiveAdvertiseStaticData {

    private var activeAdvertiseResponse: FindAllActiveAdvertiseResponse? = null

    /** classified home screen advertise **/
    private var advertiseOnClassifiedHomeScreen =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

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

    private fun addClassifiedData() {
        activeAdvertiseResponse?.forEachIndexed { index, data ->
            /** classified home screen advertise **/
            if (data.advertisementPageLocation.locationId == 2) {
                if (!advertiseOnClassifiedHomeScreen.contains(activeAdvertiseResponse!![index])) {
                    advertiseOnClassifiedHomeScreen.add(activeAdvertiseResponse!![index])
                }
            }
            /** classified landing page data **/
            if (data.advertisementPageLocation.locationId == 19) {
                if (!classifiedJustAboveFooterTextOnly.contains(activeAdvertiseResponse!![index])) {
                    classifiedJustAboveFooterTextOnly.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 18) {
                if (!classifiedJustAboveFooterImageOnly.contains(activeAdvertiseResponse!![index])) {
                    classifiedJustAboveFooterImageOnly.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 17) {
                if (!classifiedJustAboveBottomTabBOTH.contains(activeAdvertiseResponse!![index])) {
                    classifiedJustAboveBottomTabBOTH.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 16) {
                if (!classifiedTopBanner.contains(activeAdvertiseResponse!![index])) {
                    classifiedTopBanner.add(activeAdvertiseResponse!![index])
                }
            }

            /** classified details screen advertise **/
            if (data.advertisementPageLocation.locationId == 32 || data.advertisementPageLocation.locationId == 33) {
                if (!advertiseOnClassifiedDetails.contains(activeAdvertiseResponse!![index])) {
                    advertiseOnClassifiedDetails.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 34) {
                if (!advertiseOnClassifiedDetails.contains(activeAdvertiseResponse!![index])) {
                    advertiseOnClassifiedDetails.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 35) {
                if (!advertiseOnClassifiedDetails.contains(activeAdvertiseResponse!![index])) {
                    advertiseOnClassifiedDetails.add(activeAdvertiseResponse!![index])
                }
            }

            /** event landing page data **/
            if (data.advertisementPageLocation.locationId == 23) {
                if (!eventJustAboveFooterTextOnly.contains(activeAdvertiseResponse!![index])) {
                    eventJustAboveFooterTextOnly.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 22) {
                if (!eventJustAboveFooterImageOnly.contains(activeAdvertiseResponse!![index])) {
                    eventJustAboveFooterImageOnly.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 21) {
                if (!eventJustAboveBottomTabBOTH.contains(activeAdvertiseResponse!![index])) {
                    eventJustAboveBottomTabBOTH.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 20) {
                if (!eventTopBanner.contains(activeAdvertiseResponse!![index])) {
                    eventTopBanner.add(activeAdvertiseResponse!![index])
                }
            }

            /** event details screen advertise **/
            if (data.advertisementPageLocation.locationId == 36) {
                if (!advertiseOnEventDetails.contains(activeAdvertiseResponse!![index])) {
                    advertiseOnEventDetails.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 37) {
                if (!advertiseOnEventDetails.contains(activeAdvertiseResponse!![index])) {
                    advertiseOnEventDetails.add(activeAdvertiseResponse!![index])
                }
            }
            if (data.advertisementPageLocation.locationId == 38) {
                if (!advertiseOnEventDetails.contains(activeAdvertiseResponse!![index])) {
                    advertiseOnEventDetails.add(activeAdvertiseResponse!![index])
                }
            }
        }
    }

    fun getActiveAdvertiseDetails() = activeAdvertiseResponse

    /** classified data advertise **/
    fun getAdvertiseOnClassifiedHomeScreen() = advertiseOnClassifiedHomeScreen
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