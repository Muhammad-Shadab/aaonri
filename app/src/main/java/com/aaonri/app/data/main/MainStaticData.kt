package com.aaonri.app.data.main

import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponse
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem

object MainStaticData {

    private var activeAdvertiseResponse: FindAllActiveAdvertiseResponse? = null
    private var classifiedJustAboveFooterTextOnly =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()
    private var classifiedJustAboveFooterImageOnly =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()
    private var classifiedLHSBoth =
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
            }
            if (data.advertisementPageLocation.locationId == 18) {
                if (!classifiedJustAboveFooterImageOnly.contains(data)) {
                    classifiedJustAboveFooterImageOnly.add(data)
                }
            }
            if (data.advertisementPageLocation.locationId == 17) {
                if (!classifiedLHSBoth.contains(data)) {
                    classifiedLHSBoth.add(data)
                }
            }
        }
    }

    fun getClassifiedJustAboveFooterTextOnly() = classifiedJustAboveFooterTextOnly

    fun getActiveAdvertiseDetails() = activeAdvertiseResponse

}