package com.aaonri.app.data.classified.repository

import com.aaonri.app.data.classified.api.ClassifiedApi
import com.aaonri.app.data.classified.api.PostClassifiedApi
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.model.LikeDislikeClassifiedRequest
import com.aaonri.app.data.classified.model.PostClassifiedRequest
import com.aaonri.app.data.classified.model.UploadImagesRequest
import okhttp3.RequestBody
import retrofit2.http.Field
import java.io.File
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

   // suspend fun uploadImages(uploadImagesRequest: UploadImagesRequest) = postClassifiedApi.uploadImages(uploadImagesRequest)

}