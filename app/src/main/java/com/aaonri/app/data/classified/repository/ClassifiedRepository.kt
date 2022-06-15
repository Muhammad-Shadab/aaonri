package com.aaonri.app.data.classified.repository

import com.aaonri.app.data.classified.api.ClassifiedApi
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import javax.inject.Inject

class ClassifiedRepository @Inject constructor(private val classifiedApi: ClassifiedApi) {

    /*suspend fun getAllUserAdsClassified(email: String) = classifiedApi.allUserAdsClassified(email)*/

    suspend fun getClassifiedByUser(getClassifiedsByUserRequest: GetClassifiedByUserRequest) =
        classifiedApi.getClassifiedByUser(getClassifiedsByUserRequest)

}