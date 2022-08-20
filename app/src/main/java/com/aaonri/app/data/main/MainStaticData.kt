package com.aaonri.app.data.main

import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponse
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem
import com.aaonri.app.data.main.adapter.CheckViewType

object MainStaticData {

    private var activeAdvertiseResponse: FindAllActiveAdvertiseResponse? = null

    private var classifiedJustAboveFooterTextOnly =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var classifiedJustAboveFooterImageOnly =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var classifiedJustAboveBottomTabBOTH =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var classifiedTopBanner =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    fun updateActiveAdvertiseDetails(value: FindAllActiveAdvertiseResponse?) {
        activeAdvertiseResponse = value
        addClassifiedData()
    }

    fun addClassifiedData() {
        activeAdvertiseResponse?.forEach { data ->
            if (data.advertisementPageLocation.locationId == 19) {
                if (!classifiedJustAboveFooterTextOnly.contains(data)) {
                    classifiedJustAboveFooterTextOnly.add(data)
                }
                classifiedJustAboveFooterTextOnly.forEach {
                    it.checkViewType = CheckViewType.TEXT_ONLY
                }
            }
            if (data.advertisementPageLocation.locationId == 18) {
                if (!classifiedJustAboveFooterImageOnly.contains(data)) {
                    classifiedJustAboveFooterImageOnly.add(data)
                }
                classifiedJustAboveFooterImageOnly.forEach {
                    it.checkViewType = CheckViewType.IMAGE_ONLY
                }
            }
            if (data.advertisementPageLocation.locationId == 17) {
                if (!classifiedJustAboveBottomTabBOTH.contains(data)) {
                    classifiedJustAboveBottomTabBOTH.add(data)
                }
                classifiedJustAboveBottomTabBOTH.forEach {
                    it.checkViewType = CheckViewType.BOTH
                }
            }
            if (data.advertisementPageLocation.locationId == 16) {
                if (!classifiedTopBanner.contains(data)) {
                    classifiedTopBanner.add(data)
                }
                classifiedTopBanner.forEach {
                    it.checkViewType = CheckViewType.BOTH
                }
            }
        }
    }

    fun getClassifiedJustAboveFooterTextOnly() = classifiedJustAboveFooterTextOnly
    fun getClassifiedJustAboveFooterImageOnly() = classifiedJustAboveFooterImageOnly
    fun getClassifiedJustAboveBottomTabBOTH() = classifiedJustAboveBottomTabBOTH
    fun getClassifiedTopBanner() = classifiedTopBanner

    fun getActiveAdvertiseDetails() = activeAdvertiseResponse

}