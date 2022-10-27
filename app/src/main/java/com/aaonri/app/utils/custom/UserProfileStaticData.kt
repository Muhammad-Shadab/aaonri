package com.aaonri.app.utils.custom

import com.aaonri.app.data.classified.model.FindByEmailDetailResponse

object UserProfileStaticData {

    var userProfileData: FindByEmailDetailResponse? = null

    fun setUserProfileDataValue(value: FindByEmailDetailResponse?) {
        userProfileData = value
    }

    fun getUserProfileDataValue() = userProfileData

}