package com.aaonri.app.data.main

import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponse
import com.aaonri.app.data.advertise.model.FindAllActiveAdvertiseResponseItem

object ActiveAdvertiseStaticData {

    private var activeAdvertiseResponse: FindAllActiveAdvertiseResponse? = null

    /** classified home screen advertise **/
    private var advertiseOnClassifiedHomeScreen =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()


    private var classifiedTopBannerAds =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var classifiedBottomAds =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    /** classified details screen advertise **/
    private var advertiseOnClassifiedDetails =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    /** event landing page data **/
    private var eventTopBannerAds =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    private var eventBottomAds =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()

    /** event details screen advertise **/
    private var advertiseOnEventDetails =
        mutableListOf<FindAllActiveAdvertiseResponseItem>()


    fun setClassifiedAdsData(
        topBannerAds: MutableList<FindAllActiveAdvertiseResponseItem>,
        bottomAds: MutableList<FindAllActiveAdvertiseResponseItem>,
        detailsScreenAds: MutableList<FindAllActiveAdvertiseResponseItem>
    ) {
        classifiedTopBannerAds = topBannerAds
        classifiedBottomAds = bottomAds
        advertiseOnClassifiedDetails = detailsScreenAds
    }

    fun setEventAdsData(
        topBannerAds: MutableList<FindAllActiveAdvertiseResponseItem>,
        bottomAds: MutableList<FindAllActiveAdvertiseResponseItem>,
        detailsScreenAds: MutableList<FindAllActiveAdvertiseResponseItem>
    ) {
        eventTopBannerAds = topBannerAds
        eventBottomAds = bottomAds
        advertiseOnEventDetails = detailsScreenAds
    }

    fun getActiveAdvertiseDetails() = activeAdvertiseResponse

    /** classified data advertise **/
    fun getClassifiedTopBannerAds() = classifiedTopBannerAds
    fun getClassifiedBottomAds() = classifiedBottomAds
    fun getAdvertiseOnClassifiedDetails() = advertiseOnClassifiedDetails
    //fun getClassifiedTopBanner() = classifiedTopBanner

    /** event data advertise **/
    fun getEventTopBannerAds() = eventTopBannerAds
    fun getEventBottomAds() = eventBottomAds
    fun getAdvertiseOnEventDetails() = advertiseOnEventDetails

}