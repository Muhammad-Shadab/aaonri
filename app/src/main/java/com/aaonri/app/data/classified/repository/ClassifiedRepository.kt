package com.aaonri.app.data.classified.repository

import com.aaonri.app.data.classified.api.ClassifiedApi
import com.aaonri.app.data.classified.api.PostClassifiedApi
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.model.LikeDislikeClassifiedRequest
import com.aaonri.app.data.classified.model.PostClassifiedRequest
import javax.inject.Inject

class ClassifiedRepository @Inject constructor(
    private val classifiedApi: ClassifiedApi,
    private val postClassifiedApi: PostClassifiedApi
) {

    /*suspend fun getAllUserAdsClassified(email: String) = classifiedApi.allUserAdsClassified(email)*/

    suspend fun getClassifiedCategory() = postClassifiedApi.getClassifiedCategory()

    suspend fun getClassifiedByUser(getClassifiedsByUserRequest: GetClassifiedByUserRequest) =
        classifiedApi.getClassifiedByUser(getClassifiedsByUserRequest)

    suspend fun postClassified(postClassifiedRequest: PostClassifiedRequest) =
        postClassifiedApi.postClassified(postClassifiedRequest)

    suspend fun likeDislikeClassified(likeDislikeClassifiedRequest: LikeDislikeClassifiedRequest) =
        classifiedApi.likedDislikeClassified(likeDislikeClassifiedRequest)

}