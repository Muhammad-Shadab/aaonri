package com.aaonri.app.data.classified.repository

import com.aaonri.app.data.classified.api.ClassifiedApi
import javax.inject.Inject

class ClassifiedRepository @Inject constructor(private val classifiedApi: ClassifiedApi) {

    suspend fun getAllUserAdsClassified(email: String) = classifiedApi.allUserAdsClassified(email)

}