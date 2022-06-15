package com.aaonri.app.data.classified.repository

import com.aaonri.app.data.classified.api.ClassifiedApi
import com.aaonri.app.data.classified.api.PostClassifiedApi
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import javax.inject.Inject

class ClassifiedRepository @Inject constructor(
    private val classifiedApi: ClassifiedApi,
    private val postClassifiedApi: PostClassifiedApi
) {

    /*suspend fun getAllUserAdsClassified(email: String) = classifiedApi.allUserAdsClassified(email)*/

    suspend fun getClassifiedCategory() = postClassifiedApi.getClassifiedCategory()

    suspend fun getClassifiedByUser(getClassifiedsByUserRequest: GetClassifiedByUserRequest) =
        classifiedApi.getClassifiedByUser(getClassifiedsByUserRequest)

}