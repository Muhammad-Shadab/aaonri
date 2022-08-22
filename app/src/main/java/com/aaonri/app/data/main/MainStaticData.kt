package com.aaonri.app.data.main

import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponse
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.main.adapter.CheckViewType

object MainStaticData {

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
    private var classifiedAdvertiseDetails =
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
                if (!classifiedAdvertiseDetails.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.BOTH
                }
            }
            if (data.advertisementPageLocation.locationId == 34) {
                if (!classifiedAdvertiseDetails.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.IMAGE_ONLY
                }
            }
            if (data.advertisementPageLocation.locationId == 35) {
                if (!classifiedAdvertiseDetails.contains(activeAdvertiseResponse!![index])) {
                    activeAdvertiseResponse!![index].checkViewType = CheckViewType.TEXT_ONLY
                }
            }
        }
    }

    fun getActiveAdvertiseDetails() = activeAdvertiseResponse

    fun getClassifiedJustAboveFooterTextOnly() = classifiedJustAboveFooterTextOnly
    fun getClassifiedJustAboveFooterImageOnly() = classifiedJustAboveFooterImageOnly
    fun getClassifiedJustAboveBottomTabBOTH() = classifiedJustAboveBottomTabBOTH
    fun getClassifiedAdvertiseDetails() = classifiedAdvertiseDetails

    fun getClassifiedTopBanner() = classifiedTopBanner

}