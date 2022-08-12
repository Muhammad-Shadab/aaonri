package com.aaonri.app.data.advertise

import com.aaonri.app.data.advertise.model.AdvertiseDetailsResponse

object AdvertiseStaticData {

    private var advertiseDetails: AdvertiseDetailsResponse? = null

    fun updateAdvertiseDetails(value: AdvertiseDetailsResponse?) {
        advertiseDetails = value
    }

    fun getAddDetails() = advertiseDetails

}